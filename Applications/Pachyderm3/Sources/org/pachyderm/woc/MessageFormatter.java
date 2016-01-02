/*
 * WOMessageFormatter.java
 * A simple display component using java.text.MessageFormat
 *
 *  Written by Matt Firlik -- Feb21/03
 *  Minor corrections by Gavin Eadie -- Jun30/03
 *  Add array of parameters by Gavin Eadie -- Dec12/03
 */

package org.pachyderm.woc;

import java.text.MessageFormat;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSPropertyListSerialization;

/**
 * This component can be used to create a formatted string (directly in a component)
 * using a series of bindings.  Using the MessageFormat class, this component creates
 * a single string from the information obtained from two (or more) bindings.  The
 * general usage may be either:
 * <p>
 * <pre>
 * Format: WOMessageFormatter {
 *    value = "abc{0}ghi{1}";
 *    valueWhenEmpty = "not specified";
 *    param0 = "def";
 *    param1 = "jkl";
 * }
 * </pre>
 *
 * or:
 * <p>
 * <pre>
 * Format: WOMessageFormatter {
 *    value = "abc{0}ghi{1}";
 *    array = <array of parameters>;
 * }
 * </pre>
 * Simply specify a format string, and then any number of parameter bindings, with
 * the form "param#", where # represents the index number of the parameter for the
 * format string.  There are no mechanisms in place to ensure the format string is 
 * valid, or that the correct number of parameters have been specified -- however, you
 * do not need to use sequential index numbers.  (You can have "param1" and "param4"
 * bindings without "param0", "param2", and "param3" -- just as long as you reference
 * the correct indexes in your format string.
 * <br /><br />
 * You can also pass a different component instance for the bindings, if you want.
 * Rather, if you want the information for the formatter to come from the parent
 * component, you can use:
 * <p>
 * <pre>
 * Format: WOMessageFormatter {
 *    source = <method returning a component>;
 * }
 * </pre>
 *
 * As of JDK v1.3.x, it seems there is a limitation on the number of params you can
 * use.  The limit is 10 (up to index 9, since the indexes are 0-based).
 */

public class MessageFormatter extends WOComponent {
  private static final Logger   LOG = LoggerFactory.getLogger(MessageFormatter.class);
  private static final long     serialVersionUID = 7373827858957324058L;

  @SuppressWarnings("unused")
  private static final String _VERSION = "1.4";

  private static final String _SOURCE_KEY = "source"; // Key for the format binding source
  private static final String _ESCAPE_KEY = "escapeHTML"; // Key for the HTML escape binding
  private static final String _VALUE_KEY = "value"; // Key for the formatter binding
  private static final String _NOVALUE_KEY = "valueWhenEmpty";
  private static final String _SUB_KEY = "param"; // Key for the sub (arguments) bindings prefix
  private static final String _ARRAY_KEY = "array"; // Key for the array of parameters binding
  private static final String _LENGTH_KEY = "length"; // (Optional)
  private static final String _EMPTY_STRING = ""; // Empty string for insertions

  private static final int _SUB_KEY_LENGTH = _SUB_KEY.length();
  private static final int _DEFAULT_INDEX = -1;

  protected WOComponent _source; // (Optional) source component to use

/**
 * Constructor
 */

  public MessageFormatter(WOContext context) {
    super(context);
    LOG.trace("+++ constructor");
  }

/**
 * Returns the formatted string ("value" for the WOString).  This pulls the formatter
 * and the arguments from the internal convenience methods, then parses the content
 * using the MessageFormat class.  This method is performed each time the component
 * is rendered (no caching is involved).
 *
 * @return      parsed format value
 */

  public String formattedString() {
    String      formatter = _formatter();   // get the formatter
    Object      args[] = _arguments();      // get the arguments
    int         length = _length();         // get any truncation

    String result = (length > 0) ? StringUtils.left(MessageFormat.format(formatter, args), length) : 
      MessageFormat.format(formatter, args);
    return result;
  }

  /**
   * Returns the HTML escape boolean ("escapeHTML" for the WOString).
   *
   * @return      escapeHTML
   */
  
  public Boolean escapeHTML() {
    return _source().valueForBooleanBinding(_ESCAPE_KEY, true);
  }
  
/**
 * Returns the formatting string from the bindings, or "" if missing.
 *
 * @return      formatter from the bindings
 */

  private String _formatter() {
    Object formatString = _source().valueForBinding(_VALUE_KEY);
    if (null == formatString) {
      String noValueString = (String)_source().valueForBinding(_NOVALUE_KEY);
      if (null == noValueString) {
        LOG.error("*** value/novalue bindings are both null !");
        formatString = _EMPTY_STRING;
      } else {
        formatString = noValueString;
      }
    }
    else {
      if (!(formatString instanceof String)) {
        LOG.warn("value binding is a " + formatString.getClass().getName() + ".");
        formatString = formatString.toString();
      }
    }

    LOG.trace("FORMAT: " + (String) formatString);
    return (String) formatString;
  }

/**
 * Returns the argument array for the formatter to use.  This method iterates through
 * the bindings and looks for keys which have the specified substitute prefix.  This
 * method assumes the binding is of the form "<prefix>##", where ## is a number that
 * corresponds to the index value for the parameter.  Any bindings which do not match
 * this format are ignored.
 *
 * Note that this returns an array fit to hold the binding values.  Hence, if the 
 * bindings found are "<prefix>1" and "<prefix>9", the array will contain ten items
 * (since the array must be large enough for the index values).  All slots without a
 * binding will have an empty string.
 *
 * @return      formatter from the bindings
 */

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private Object[] _arguments() {
    Vector arguments = new Vector(20); // Create a vector for the values

    if (hasBinding(_ARRAY_KEY)) {
      Object    array = _source().valueForBinding( _ARRAY_KEY );
      if (array instanceof NSArray) {
        arguments = ((NSArray)array).vector();
      }
      if (array instanceof String[]) {
        return (String[])array;
      }
      if (array instanceof String) {
        arguments = ((NSArray<String>)NSPropertyListSerialization.arrayForString((String)array)).vector();
      }
    }
    else {
      for (String bindingName : _source().bindingKeys()) {              // Enumerate the list
        if (bindingName.startsWith(_SUB_KEY)) {                         // Ensure it begins with the expected value

          int index = _DEFAULT_INDEX;                                   // Default index for insertion
          
          try {                                                         // Attempt to get the index
            index = Integer.parseInt(bindingName.substring(_SUB_KEY_LENGTH));
          } catch (Exception e) { }

          if (index != _DEFAULT_INDEX) {                                // If the index is palatable
            Object binding = _source().valueForBinding(bindingName);    // Get the binding from the associations
            if (binding == null) {
              binding = _EMPTY_STRING;
            }
            
            _insertBindingIntoArrayAtIndex(binding, arguments, index);  // Insert it into the correct location
          }
        }
      }
    }
    return arguments.toArray();                                         // Return the params
  }

/**
 * Returns the source component to use for the formatting information.  If the information for
 * the formatter is not present in the bindings, the "source" binding can be used to tell the
 * formatter where the bindings ARE (or at least where to look).  In most cases, this means
 * the values will come from bindings in the parent component -- meaning the parent has to send
 * itself as a binding (somewhat scary).
 *
 * This value is pulled once per cycle of the request-response loop and cached, so we don't 
 * accidentally pull a different value mid-stream.
 *
 * @return      source component for the binding information
 */

  private WOComponent _source() {
    if (this._source == null) { // If the value hasn't been pulled yet

// NOTE:  this could be changed.  Instead of passing the component as a binding, you could pass a key to use 
// to get the component.  For example, you could pass the key "parent", and then use valueForKey( _SOURCE_KEY ) 
// instead of valueForBinding.  This has the advantage of not requiring the parent component to implement a 
// method to return itself (to set itself into the binding).

      this._source = (hasBinding(_SOURCE_KEY) ? (WOComponent) valueForBinding(_SOURCE_KEY) : this);
      LOG.trace("SOURCE: " + this._source.name());
    }

    return this._source; // Return the value
  }

/**
 * Returns the maximum length of the formatted information.  If this binding is not
 * provided, the output string will not be clipped.
 *
 * @return      length for the output string (-1 if missing or formatted wrong, >3 otherwise)
 */

  private int _length() {
    Object      _length = _source().valueForBinding(_LENGTH_KEY);
    if ( _length == null) {
      return -1;
    }
    if (_length instanceof String) {
      try {
        return Integer.parseInt((String) _length);
      }
      catch (Exception x) { }
    }
    return -1;
  }

/**
 * Method to insert the specified binding into the array at the appropriate index.  Since we
 * have no way of knowing what the index value specified was, or if it is within the bounds
 * of the current array, we must check the value against the array size.  If the insertion
 * point is past the array size, we add empty strings into the array until the array is large
 * enough to handle the insert.
 *
 * @param binding       the binding to insert
 * @param array         the array to insert the string into
 * @param index         the index at which to insert the binding into the array
 */

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void _insertBindingIntoArrayAtIndex (Object binding, Vector vector, int index) {
    if ((binding != null) && (vector != null)) {        // The binding and vector cannot be null
      int limit = index + 1;
      if (limit > vector.size()) {
        vector.setSize(limit);
      }
      vector.set (index, binding);            // Insert the binding value
    }
  }

/**
 * Implementation of the reset() method, as defined by stateless components.  This method is
 * performed at the end of each stage of the request-response loop, which ensures that we 
 * are not caching a value that is improper;  in this case, we clear the source component
 * (if there is one set -- there may not be)
 */

  @Override
  public void reset() {
    super.reset(); // Call super, then clear
    this._source = null;
  }

  /** 
   * Make the component stateless and non-synchronizing.
   */

  @Override
  public boolean isStateless() {
    return true;
  }
}
