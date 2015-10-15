/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * A javascript object for a Location
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SimpleScriptable extends ScriptableObject {
    private static final Map PROPERTY_MAPS = Collections.synchronizedMap( new HashMap(89) );

    private JavaScriptEngine.PageInfo pageInfo_;
    private HtmlElement htmlElement_;

    private class PropertyInfo {
        private Method getter_;
        private Method setter_;
        private FunctionObject function_;
    }

    private static final String[][] HTML_JAVASCRIPT_MAPPING = {
        {"HtmlButton", "Button"},
        {"HtmlCheckBox", "Checkbox"},
        {"HtmlFileInput", "FileUpload"},
        {"HtmlForm", "Form"},
        {"HtmlHiddenInput", "Hidden"},
        {"HtmlImage", "Image"},
        {"HtmlOption", "Option"},
        {"HtmlPasswordInput", "Password"},
        {"HtmlRadioButtonInput", "Radio"},
        {"HtmlResetInput", "Reset"},
        {"HtmlSelect", "Select"},
        {"HtmlSubmitInput", "Submit"},
        {"HtmlTextInput", "Text"},
        {"HtmlTextArea", "Textarea"} };


    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public SimpleScriptable() {
    }


    private Map getPropertyMap() {
        synchronized( PROPERTY_MAPS ) {
            final Class clazz = getClass();
            final String className = clazz.getName();
            Map localPropertyMap = (Map)PROPERTY_MAPS.get(className);
            if( localPropertyMap == null && htmlElement_ != null ) {
                try {
                    localPropertyMap = createLocalPropertyMap();
                }
                catch( final Exception e ) {
                    throw new ScriptException(e);
                }
                PROPERTY_MAPS.put( className, localPropertyMap );
            }
            return localPropertyMap;
        }
    }


    private JavaScriptConfiguration getJavaScriptConfiguration() {
        final BrowserVersion browserVersion
            = getHtmlElementOrDie().getPage().getWebClient().getBrowserVersion();
        return JavaScriptConfiguration.getInstance(browserVersion);
    }


    private Map createLocalPropertyMap() throws Exception {
        final Class clazz = getClass();
        final JavaScriptConfiguration configuration = getJavaScriptConfiguration();
        final List readableProperties = configuration.getReadablePropertyNames(clazz);
        final List writableProperties = configuration.getWritablePropertyNames(clazz);
        final List functions = configuration.getFunctionNames(clazz);

        final Map localPropertyMap = new HashMap(89);

        final Method[] methods = getClass().getMethods();
        for( int i=0; i<methods.length; i++ ) {
            final String methodName = methods[i].getName();
            if( methodName.startsWith("jsGet_")  && methods[i].getParameterTypes().length == 0 ) {
                final String propertyName = methodName.substring(6);
                if( readableProperties.contains(propertyName) ) {
                    getPropertyInfo(localPropertyMap, propertyName).getter_ = methods[i];
                }
                else {
                    getLog().trace("Getter for ["+propertyName+"] not used by this browser version");
                }
            }
            else if( methodName.startsWith("jsSet_")
                && methods[i].getParameterTypes().length == 1 ) {

                final String propertyName = methodName.substring(6);
                if( writableProperties.contains(propertyName) ) {
                    getPropertyInfo(localPropertyMap, propertyName).setter_ = methods[i];
                }
                else {
                    getLog().trace("Setter for ["+propertyName+"] not used by this browser version");
                }
            }
            else if( methodName.startsWith("jsFunction_") ) {
                final String functionName = methodName.substring("jsFunction_".length());
                if( functions.contains(functionName) ) {
                    final FunctionObject functionObject
                        = new FunctionObject(functionName, methods[i], this);
                    getPropertyInfo(localPropertyMap, functionName).function_ = functionObject;
                }
                else {
                    getLog().trace("Function ["+functionName+"] not used by this browser version");
                }
            }
        }
        return localPropertyMap;
    }


    private PropertyInfo getPropertyInfo( final Map localPropertyMap, final String name ) {
        PropertyInfo info = (PropertyInfo)localPropertyMap.get(name);
        if( info == null ) {
            info = new PropertyInfo();
            localPropertyMap.put(name, info);
        }
        return info;
    }


    /**
     * Return the javascript class name
     * @return The javascript class name
     */
    public String getClassName() {
        final String javaClassName = getClass().getName();
        final int index = javaClassName.lastIndexOf(".");
        if( index == -1 ) {
            throw new IllegalStateException("No dot in classname: "+javaClassName);
        }

        return javaClassName.substring(index+1);
    }


    /**
     * Assert that the specified parameter is not null.  Throw a NullPointerException
     * if a null is found.
     * @param description The description to pass into the NullPointerException
     * @param object The object to check for null.
     */
    protected final void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }


    /**
     * Set the page info.  This contains information specific to the rhino engine.
     * @param pageInfo The new pageInfo.
     */
    public final void setPageInfo( final JavaScriptEngine.PageInfo pageInfo ) {
        assertNotNull("pageInfo", pageInfo);
        pageInfo_ = pageInfo;
    }


    /**
     * Return the javascript engine that is executing this code.
     * @return The engine
     */
    public JavaScriptEngine getJavaScriptEngine() {
        return getPageInfo().getJavaScriptEngine();
    }


    /**
     * Create a new javascript object
     * @param className The class name of the new object
     * @return The new object
     */
    public SimpleScriptable makeJavaScriptObject( final String className ) {
        final JavaScriptEngine.PageInfo pageInfo = getPageInfo();

        final SimpleScriptable newObject;
        try {
            newObject = (SimpleScriptable)pageInfo.getContext().newObject(
                pageInfo.getScope(), className, new Object[0]);
            newObject.setPageInfo( pageInfo );
            return newObject;
        }
        catch( final Exception e ) {
            throw new ScriptException(e);
        }
    }


    /**
     * Return the html element that corresponds to this javascript object or throw an exception
     * if one cannot be found.
     * @return The html element
     * @exception IllegalStateException If the html element could not be found.
     */
     public final HtmlElement getHtmlElementOrDie() throws IllegalStateException {
         if( htmlElement_ == null ) {
             throw new IllegalStateException(
                "HtmlElement has not been set for this SimpleScriptable: "+getClass().getName());
         }
         else {
             return htmlElement_;
         }
     }


     /**
      * Set the html element that corresponds to this javascript object
      * @param htmlElement The html element
      */
     public void setHtmlElement( final HtmlElement htmlElement ) {
         assertNotNull("htmlElement", htmlElement);
         htmlElement_ = htmlElement;
         htmlElement_.setScriptObject(this);
     }


     /**
      * Return the specified property or NOT_FOUND if it could not be found.
      * @param name The name of the property
      * @param start The scriptable object that was originally queried for this property
      * @return The property.
      */
     public Object get( final String name, final Scriptable start ) {
         // Some calls to get will happen during the initialization of the superclass.
         // At this point, we don't have enough information to do our own initialization
         // so we have to just pass this call through to the superclass.
         if( htmlElement_ == null ) {
             return super.get(name, start);
         }

         final PropertyInfo info = (PropertyInfo)getPropertyMap().get(name);
         if( info == null || ( info.getter_ == null && info.function_ == null ) ) {
             final JavaScriptConfiguration configuration = getJavaScriptConfiguration();
             if( configuration.getReadablePropertyNames(getClass()).contains(name) ) {
                 getLog().warn("Getter not implemented for property ["+name+"]");
             }
             else if( configuration.getFunctionNames(getClass()).contains(name) ) {
                 getLog().warn("Function not implemented ["+name+"]");
             }

             final Object result = super.get(name, start);
             if( result == Context.getUndefinedValue() ) {
                 getLog().trace("Getter/function not found for name ["+name+"]");
             }
             //getLog().trace("** Getter/function not found for name ["+name+"]");

             return result;
         }

         if( info.function_ != null ) {
             return info.function_;
         }

         try {
             return info.getter_.invoke( this, new Object[0] );
         }
         catch( final Exception e ) {
             throw new ScriptException(e);
         }
     }


     /**
      * Set the specified property
      * @param name The name of the property
      * @param start The scriptable object that was originally invoked for this property
      * @param newValue The new value
      */
     public void put( final String name, final Scriptable start, Object newValue ) {
         // Some calls to put will happen during the initialization of the superclass.
         // At this point, we don't have enough information to do our own initialization
         // so we have to just pass this call through to the superclass.
         if( htmlElement_ == null ) {
             super.put(name, start, newValue);
             return;
         }

         final PropertyInfo info = (PropertyInfo)getPropertyMap().get(name);
         if( info == null || info.setter_ == null ) {
             final JavaScriptConfiguration configuration = getJavaScriptConfiguration();
             if( configuration.getReadablePropertyNames(getClass()).contains(name) ) {
                 getLog().warn("Setter not implemented for property ["+name+"]");
             }

             super.put(name, start, newValue);
         }
         else {
             final String className = getClass().getName();
             final Class parameterClass = info.setter_.getParameterTypes()[0];
             if( parameterClass == "".getClass() ) {
                 newValue = newValue.toString();
             }
             try {
                 info.setter_.invoke(
                    findMatchingScriptable(start, info.setter_), new Object[]{ newValue } );
             }
             catch( final InvocationTargetException e ) {
                 throw new ScriptException(e.getTargetException());
             }
             catch( final Exception e ) {
                 throw new ScriptException(e);
             }
         }
     }


     /**
      * Walk up the prototype chain and return the first scriptable that this method can
      * be invoked on.
      */
     private Scriptable findMatchingScriptable( final Scriptable start, final Method method ) {
         final Class declaringClass = method.getDeclaringClass();
         Scriptable scriptable = start;
         while( declaringClass.isInstance(start) == false ) {
             scriptable = scriptable.getPrototype();
             if( scriptable == null ) {
                 throw new IllegalStateException("Couldn't find a matching scriptable");
             }
         }

         return scriptable;
     }


     /**
      * Return the log that is being used for all scripting objects
      * @return The log.
      */
     protected final Log getLog() {
         return LogFactory.getLog(getClass());
     }


     private JavaScriptEngine.PageInfo getPageInfo() {
         if( pageInfo_ == null ) {
             throw new IllegalStateException("pageInfo_ has not been initialized!");
         }
         return pageInfo_;
     }


     /**
      * Return the javascript object that corresponds to the specified html element.
      * New javascript objects will be created as needed.  If a javascript object
      * cannot be created for this htmlElement then NOT_FOUND will be returned.
      *
      * @param htmlElement The html element
      * @return The javascript object or NOT_FOUND
      */
    protected Object getScriptableFor( final HtmlElement htmlElement ) {
        final Object scriptObject = htmlElement.getScriptObject();
        if( scriptObject != null ) {
            return scriptObject;
        }

        final String fullClassName = htmlElement.getClass().getName();
        final String className = fullClassName.substring( fullClassName.lastIndexOf(".") + 1 );

        for( int i=0; i<HTML_JAVASCRIPT_MAPPING.length; i++ ) {
            if( className.equals(HTML_JAVASCRIPT_MAPPING[i][0]) ) {
                final SimpleScriptable scriptable = makeJavaScriptObject(HTML_JAVASCRIPT_MAPPING[i][1]);
                scriptable.setHtmlElement(htmlElement);
                return scriptable;
            }
        }

        return NOT_FOUND;
    }
}
