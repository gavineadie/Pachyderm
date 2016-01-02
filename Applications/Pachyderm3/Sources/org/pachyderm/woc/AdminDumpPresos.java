package org.pachyderm.woc;

import org.pachyderm.foundation.eof.PDBComponent;
import org.pachyderm.foundation.eof.PDBPresentation;
import org.pachyderm.foundation.eof.PDBScreen;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableSet;

public class AdminDumpPresos extends AdminHelper {
  private static final long           serialVersionUID = -6563696629241559730L;

  private EOEditingContext            _xec = session().defaultEditingContext();
  
  public PDBPresentation              currentPreso;
  
  public PDBScreen                    currentScreen;
  public Integer                      screenIndex;
  
  private NSMutableSet<PDBComponent>  everyElement = new NSMutableSet<PDBComponent>();
  public PDBComponent                 currentElement;
  public Integer                      elementIndex;
  
  public AdminDumpPresos(WOContext context) {
    super(context);
  }

  @Override
  public boolean isStateless() {
    return true;
  }
  
  @Override
  public void reset() {
    _xec.reset();
    _xec = null;
    
    currentPreso = null;
    currentScreen = null;
    screenIndex = null;
    currentElement = null;
    elementIndex = null;
    
    everyElement.removeAllObjects();
    everyElement = null;
  }
  
  public NSArray<PDBPresentation> allThePresos() {
    return PDBPresentation.fetchAllPDBPresentations(_xec);
  }

  public NSArray<PDBScreen> allTheScreens() {
    return currentPreso.everyScreen();
  }

  public Boolean primeScreen() {
    return (screenIndex == 0);  
  }
}