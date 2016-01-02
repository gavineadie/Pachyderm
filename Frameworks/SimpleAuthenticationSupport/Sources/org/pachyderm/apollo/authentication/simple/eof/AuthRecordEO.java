package org.pachyderm.apollo.authentication.simple.eof;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

public class AuthRecordEO extends _AuthRecordEO {
  private static Logger         LOG = LoggerFactory.getLogger(AuthRecordEO.class);
  private static final long     serialVersionUID = -4024894706724360020L;
    
  private Boolean               logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);

  /*------------------------------------------------------------------------------------------------*
   *  Overridden by subclasses to perform additional initialization on the receiver upon its being 
   *  fetched from the external repository into EOEditingContext. EOCustomObject's implementation 
   *  merely sends an awakeObjectFromFetch to the receiver's EOClassDescription. 
   *  
   *  Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromFetch(EOEditingContext ec) {
    super.awakeFromFetch(ec);
    
    if (logInAwake) {
      LOG.info("----->  awakeFromFetch: ({}) EOs: ({}), +({}), ~({}), -({})", 
          ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()), 
          ec.registeredObjects().count(), ec.insertedObjects().count(), 
          ec.updatedObjects().count(), ec.deletedObjects().count());
    
      @SuppressWarnings("unchecked")
      NSArray<ERXGenericRecord> genericRecords = ec.registeredObjects();
      for (ERXGenericRecord genericRecord : genericRecords) LOG.info("        EOs: " + genericRecord);
    }
  }

  /*------------------------------------------------------------------------------------------------*
   * Overridden by subclasses to perform additional initialization on the receiver upon its being 
   * inserted into EOEditingContext. This is commonly used to assign default values or record the 
   * time of insertion. EOCustomObject's implementation merely sends an awakeObjectFromInsertion 
   * to the receiver's EOClassDescription.
   * 
   * Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromInsertion(EOEditingContext ec) {
    super.awakeFromInsertion(ec);
    
    if (logInAwake) {
      LOG.info("-----> awakeFromInsert: ({}) EOs: ({}), +({}), ~({}), -({})",
          ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()), 
          ec.registeredObjects().count(), ec.insertedObjects().count(), 
          ec.updatedObjects().count(), ec.deletedObjects().count());
    }
  }
  
  /*------------------------------------------------------------------------------------------------*
   * cover methods for dealing with password as a String instead of an NSData ..
   *------------------------------------------------------------------------------------------------*/
  private static MessageDigest         _md;

  private static AuthRecordEO createAuthRecord(EOEditingContext xec, 
                                                        String password, 
                                                        String realm, 
                                                        String username) {
    AuthRecordEO eo = (AuthRecordEO) EOUtilities.createAndInsertInstance(xec, _AuthRecordEO.ENTITY_NAME);    
    eo.setUsername(username);
    eo.setRealm(realm);
    eo.setPassword(passwordToHash(password));
    return eo;
  }

  public static boolean insertAuthRecord(EOEditingContext xec, String username, String realm, String password) {
    try {
      AuthRecordEO.createAuthRecord(xec, passwordToHash(password), realm, username);
      xec.saveChanges();
      LOG.info("insertAuthRecord: SUCCESS");
      return true;
    }
    catch (Exception x) {
      xec.revert();
      LOG.warn("insertAuthRecord: FAILURE .. ", x);
    }
    return false;
  }
  
  public static AuthRecordEO getAuthRecord(EOEditingContext xec, String username, String password) {
    EOQualifier       userAndPass = ERXQ.and (ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), 
                                    ERXQ.equals(AuthRecordEO.PASSWORD_KEY, passwordToHash(password)));
    return AuthRecordEO.fetchAuthRecord(xec, userAndPass);
  }
  
  public boolean setPassword(EOEditingContext xec, String pw) {
    setPassword(passwordToHash(pw));
    try {
      xec.saveChanges();
      LOG.info("updateAuthRecord: SUCCESS");
      return true;
    }
    catch (Exception x) {
      xec.revert();
      LOG.warn("updateAuthRecord: FAILURE .. ", x);
    }
    return false;
  }
  
  private static NSData passwordToHash(String pw) {
    if (pw == null) return null;
    MessageDigest     md = getMD5MessageDigest();
    if (md == null) return null;
    
    md.reset();
    try {
      md.update(pw.getBytes("UTF-8"));
      return new NSData(md.digest());
    }
    catch (UnsupportedEncodingException x) {
      LOG.error("MessageDigest.update('UTF-8'): ", x);
    }
    return null;
  }
  
  private static MessageDigest getMD5MessageDigest() {
    if (_md == null) {
      try {
        _md = MessageDigest.getInstance("MD5");
      }
      catch (NoSuchAlgorithmException x) { 
        LOG.error("MessageDigest.getInstance('MD5'): ", x);
        _md = null; 
      }
    }
    return _md;
  }

  /*------------------------------------------------------------------------------------------------*
   * Chuck Hill : returns the changes between the current EO and the last committed version ..
   *------------------------------------------------------------------------------------------------*/
  public NSDictionary changedProperties() {
    return editingContext().committedSnapshotForObject(this);
  }
}
