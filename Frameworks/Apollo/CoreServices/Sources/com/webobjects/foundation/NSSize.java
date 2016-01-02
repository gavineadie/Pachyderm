//
//  NSSize.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Sun Aug 08 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package com.webobjects.foundation;

public class NSSize {

  private float _width;
  private float _height;

  // public static final NSSize Resolution72DPI = new NSSize(72.0f, 72.0f);
  // public static final NSSize Resolution96DPI = new NSSize(96.0f, 96.0f);
  public static final NSSize ZeroSize = new NSSize(0.0f, 0.0f);

  public NSSize() {
    _width = 0.0f;
    _height = 0.0f;
  }

  public NSSize(float width, float height) {
    _width = width; if (_width < 0.0f) _width = 0.0f;
    _height = height; if (_height < 0.0f) _height = 0.0f;
  }

  public NSSize(NSSize size) {
    if (size == null) {
      _width = 0.0f;
      _height = 0.0f;
    }
    else {
      _width = size.width(); if (_width < 0.0f) _width = 0.0f;
      _height = size.height(); if (_height < 0.0f) _height = 0.0f;
    }
  }


  public float width() {
    return _width;
  }

  public float height() {
    return _height;
  }

  // Testing NSSizes
  @Override
  public boolean equals(Object object) {
    if (object != null && object instanceof NSSize) {
      return isEqualToSize((NSSize) object);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  public boolean isEmpty() {
    return (_width == 0.0f || _height == 0.0f);
  }

  public boolean isEqualToSize(NSSize size) {
    return (size != null && size.width() == _width && size.height() == _height);
  }

  // Transforming NSSizes
  public static NSSize fromString(String string) {
    float[] args = _NSGeometryUtilities._parseGeometricStringFormatAsFloats(string);
    return new NSSize(args[0], args[1]);
  }

  @Override
  public String toString() {
    return "{" + _width + ", " + _height + "}";
  }

//  public java.awt.Dimension toDimension() {
//    return new java.awt.Dimension((int) width(), (int) height());
//  }
}