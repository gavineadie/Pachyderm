package org.pachyderm.apollo.app;

import java.io.File;

import org.pachyderm.apollo.core.CoreSmartProperty;

import com.webobjects.appserver.WOContext;

import er.extensions.foundation.ERXProperties;

public class PageDecorSettings extends MCComponent {
  private static final long serialVersionUID = 4561576290028771133L;

  public PageDecorSettings(WOContext context) {
    super(context);
  }

  
  public String pachyLicense() {
    return CoreSmartProperty.smartPropertyString("decor.licenseText");
  }

  public String bodyFontFamily() {
    return ERXProperties.stringForKeyWithDefault("decor.bodyFontFamily", "Arial,Helvetica,sans-serif");
  }
  
  public String bodyTopBarColor() {
    return ERXProperties.stringForKeyWithDefault("decor.bodyTopBarColor", "white");
  }

  public String bodyGroundColor() {
    return ERXProperties.stringForKeyWithDefault("decor.bodyGroundColor", "lightgrey");
  }

  public String panelEdgeColor() {
    return ERXProperties.stringForKeyWithDefault("decor.panelEdgeColor", "grey");
  }

  public String panelGroundColor() {
    return ERXProperties.stringForKeyWithDefault("decor.panelGroundColor", "grey");
  }

  public String blokTextColor() {
    return ERXProperties.stringForKeyWithDefault("decor.blokTextColor", "white");
  }

  public String blokBackColor() {
    return ERXProperties.stringForKeyWithDefault("decor.blokBackColor", "lightgrey");
  }

  public String blokEdgeColor() {
    return ERXProperties.stringForKeyWithDefault("decor.blokEdgeColor", "blue");
  }

  public String blokTextHover() {
    return ERXProperties.stringForKeyWithDefault("decor.blokTextHover", "darkgrey");
  }

  public String blokBackHover() {
    return ERXProperties.stringForKeyWithDefault("decor.blokBackHover", "black");
  }

  public String blokTextInert() {
    return ERXProperties.stringForKeyWithDefault("decor.blokTextInert", "darkgrey");
  }

  public String blokBackInert() {
    return ERXProperties.stringForKeyWithDefault("decor.blokBackInert", "black");
  }
  
  public String pachyLogoPath() {
    return "images" + File.separator + ERXProperties.stringForKeyWithDefault("decor.pachyLogoName", "pachy3-logo.png");
  }
}