package org.pachyderm.apollo.app;

import java.io.File;

import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.foundation.PDBDocument;
import org.pachyderm.foundation.PXInfoModel;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreenModel;
import org.pachyderm.foundation.PXUtility;

import com.webobjects.appserver.WOContext;

public class MCPresoHelper extends MCPage {
  private static final long         serialVersionUID = 2644298892500159328L;

  protected final static CXDefaults defaults = CXDefaults.sharedDefaults();
  protected static final File       PRESO_FILE = new File(defaults.getString("PresosDir"));
  protected static final File       PRESO_CACHE_FILE = new File(defaults.getString("PresoCacheDir"));

  protected PXPresentationDocument  _presentationDoc = null;


  public MCPresoHelper(WOContext context) {
    super(context);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Basic lazy getters ...
   *------------------------------------------------------------------------------------------------*/
  public PXPresentationDocument getPresentationDoc() {
    if (_presentationDoc == null)
      _presentationDoc = (PXPresentationDocument) getDocument();
    return _presentationDoc;
  }

  protected PXInfoModel presoInfoModel() {
    return getPresentationDoc().getInfoModel();
  }

  protected PXScreenModel presoScreenModel() {
    return getPresentationDoc().getScreenModel();
  }


  public String getTitle() {
    PDBDocument doc = (PDBDocument) getDocument();
    return (doc == null) ? null : doc.getInfoModel().getTitle();
  }

  public void setTitle(String title) {
    ((PDBDocument)getDocument()).getInfoModel().setTitle(title);
  }

  public String getDescription() {
    PDBDocument doc = (PDBDocument) getDocument();
    return (doc == null) ? null : doc.getInfoModel().getPrimaryDescription();
  }

  public void setDescription(String descr) {
    ((PDBDocument)getDocument()).getInfoModel().setPrimaryDescription(descr);
  }


  public String presentationURL() {
    return defaults.getString("PresosURL") + "/" +
        PXUtility.keepAlphaNumericsAndDot(presoInfoModel().getTitle() + 
            presoInfoModel()._id()) + "/index.html";
  }

  public String presentationURL5() {
    return defaults.getString("PresosURL") + "/" +
        PXUtility.keepAlphaNumericsAndDot(presoInfoModel().getTitle() + 
            presoInfoModel()._id()) + "/index5.html";
  }

  public String zipURL() {
    return defaults.getString("PresosURL") + "/" +
        PXUtility.keepAlphaNumericsAndDot(presoInfoModel().getTitle() + 
            presoInfoModel()._id()) + ".zip";
  }


  /*------------------------------------------------------------------------------------------------*
   *  Presentation Style (Flash / HTML5 / Both) ...
   *------------------------------------------------------------------------------------------------*/
  public Boolean isFlash() {
    String style = presoInfoModel().getStyle();
    return (style == null || style.equals("flash") || style.equals("both"));
  }

  public Boolean isHtml5() {
    String style = presoInfoModel().getStyle();
    return (style != null && style.equals("html5"));
  }

  public Boolean isBoth() {
    String style = presoInfoModel().getStyle();
    return (style != null && style.equals("both"));
  }

  public void setFlash(boolean flag) {
    presoInfoModel().setStyle("flash");
    getDocument().saveDocument();
  }

  public void setHtml5(boolean flag) {
    presoInfoModel().setStyle("html5");
    getDocument().saveDocument();
  }

  public void setBoth(boolean flag) {
    presoInfoModel().setStyle("both");
    getDocument().saveDocument();
  }
}