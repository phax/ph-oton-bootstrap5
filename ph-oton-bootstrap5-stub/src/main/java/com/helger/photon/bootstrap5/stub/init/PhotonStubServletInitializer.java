/*
 * Copyright (C) 2025-2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.bootstrap5.stub.init;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.concurrent.ThreadSafe;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsLinkedHashSet;
import com.helger.collection.commons.ICommonsOrderedSet;
import com.helger.photon.ajax.servlet.PhotonAjaxServlet;
import com.helger.photon.api.servlet.PhotonAPIServlet;
import com.helger.photon.bootstrap5.stub.PhotonStubServletContextListener;
import com.helger.photon.core.go.GoServlet;
import com.helger.photon.core.resource.ResourceBundleServlet;
import com.helger.photon.core.servlet.AbstractObjectDeliveryHttpHandler;
import com.helger.photon.core.servlet.LogoutServlet;
import com.helger.photon.core.servlet.StreamServlet;
import com.helger.photon.core.userdata.UserStreamServlet;
import com.helger.photon.core.userdata.UserUploadServlet;
import com.helger.servlet.filter.CharacterEncodingFilter;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

/**
 * This class dynamically registers the ph-oton servlets into the {@link ServletContext} provided.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class PhotonStubServletInitializer
{
  /**
   * Name of the servlet context init parameter (<code>context-param</code> in the
   * <code>web.xml</code>) that contains a comma separated list of servlet names, that should not be
   * registered. Use the <code>SERVLET_NAME_*</code> constants of this class as values.
   *
   * @since 1.0.4
   */
  public static final String INIT_PARAMETER_DISABLED_SERVLETS = "ph-oton.stub.disabled-servlets";

  /**
   * Name of the dynamically registered {@link PhotonAjaxServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_AJAX = "PhotonAjaxServlet";
  /**
   * Name of the dynamically registered {@link PhotonAPIServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_API = "PhotonAPIServlet";
  /**
   * Name of the dynamically registered {@link StreamServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_STREAM = "StreamServlet";
  /**
   * Name of the dynamically registered {@link UserStreamServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_USER_STREAM = "UserStreamServlet";
  /**
   * Name of the dynamically registered {@link UserUploadServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_USER_UPLOAD = "UserUploadServlet";
  /**
   * Name of the dynamically registered {@link LogoutServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_LOGOUT = "LogoutServlet";
  /**
   * Name of the dynamically registered {@link ResourceBundleServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_RESOURCE_BUNDLE = "ResourceBundleServlet";
  /**
   * Name of the dynamically registered {@link GoServlet}.
   *
   * @since 1.0.4
   */
  public static final String SERVLET_NAME_GO = "GoServlet";

  private static final Logger LOGGER = LoggerFactory.getLogger (PhotonStubServletInitializer.class);
  private static final AtomicBoolean INITIALIZED = new AtomicBoolean (false);
  private static final ICommonsOrderedSet <String> ALL_SERVLET_NAMES = new CommonsLinkedHashSet <> (SERVLET_NAME_AJAX,
                                                                                                   SERVLET_NAME_API,
                                                                                                   SERVLET_NAME_STREAM,
                                                                                                   SERVLET_NAME_USER_STREAM,
                                                                                                   SERVLET_NAME_USER_UPLOAD,
                                                                                                   SERVLET_NAME_LOGOUT,
                                                                                                   SERVLET_NAME_RESOURCE_BUNDLE,
                                                                                                   SERVLET_NAME_GO);

  @PresentForCodeCoverage
  private static final PhotonStubServletInitializer INSTANCE = new PhotonStubServletInitializer ();

  private PhotonStubServletInitializer ()
  {}

  private static boolean _isServletEnabled (@NonNull final Set <String> aDisabledServletNames,
                                            @NonNull final String sServletName)
  {
    if (aDisabledServletNames.contains (sServletName))
    {
      LOGGER.info ("Not registering the servlet '" +
                   sServletName +
                   "' because it is disabled via the init parameter '" +
                   INIT_PARAMETER_DISABLED_SERVLETS +
                   "'");
      return false;
    }
    return true;
  }

  /**
   * Get the names of all servlets that should not be registered, as configured in the servlet
   * context init parameter {@link #INIT_PARAMETER_DISABLED_SERVLETS}.
   *
   * @param aSC
   *        The {@link ServletContext} to use. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty set of servlet names.
   * @since 1.0.4
   */
  @NonNull
  @ReturnsMutableCopy
  public static ICommonsOrderedSet <String> getDisabledServletNames (@NonNull final ServletContext aSC)
  {
    ValueEnforcer.notNull (aSC, "ServletContext");

    final ICommonsOrderedSet <String> ret = new CommonsLinkedHashSet <> ();
    for (final String sPart : StringHelper.getExploded (',', aSC.getInitParameter (INIT_PARAMETER_DISABLED_SERVLETS)))
    {
      final String sServletName = sPart.trim ();
      if (StringHelper.isNotEmpty (sServletName))
      {
        if (!ALL_SERVLET_NAMES.contains (sServletName))
          LOGGER.warn ("The servlet name '" +
                       sServletName +
                       "' provided in the init parameter '" +
                       INIT_PARAMETER_DISABLED_SERVLETS +
                       "' is unknown. Known names are " +
                       ALL_SERVLET_NAMES);
        ret.add (sServletName);
      }
    }
    return ret;
  }

  /**
   * @return <code>true</code> if the servlet registration already took place, <code>false</code>
   *         otherwise.
   */
  public static boolean areServletsRegistered ()
  {
    return INITIALIZED.get ();
  }

  /**
   * Register all ph-oton servlets to the passed {@link ServletContext}.
   *
   * @param aSC
   *        The {@link ServletContext} to use. May not be <code>null</code>.
   */
  public static void registerServlets (@NonNull final ServletContext aSC)
  {
    ValueEnforcer.notNull (aSC, "ServletContext");

    if (INITIALIZED.compareAndSet (false, true))
    {
      // Check SC version
      if (aSC.getMajorVersion () < 3)
        throw new IllegalStateException ("At least servlet version 3 is required! Currently running version " +
                                         aSC.getMajorVersion () +
                                         "." +
                                         aSC.getMinorVersion ());

      LOGGER.info ("Registering default ph-oton listeners and servlets");

      final ICommonsOrderedSet <String> aDisabledServletNames = getDisabledServletNames (aSC);

      {
        final FilterRegistration.Dynamic aFilter = aSC.addFilter ("CharacterEncodingFilter",
                                                                  CharacterEncodingFilter.class);
        if (aFilter != null)
        {
          // Filter is new
          aFilter.setAsyncSupported (true);
          aFilter.setInitParameter (CharacterEncodingFilter.INITPARAM_ENCODING, StandardCharsets.UTF_8.name ());
          aFilter.setInitParameter (CharacterEncodingFilter.INITPARAM_FORCE_ENCODING, Boolean.TRUE.toString ());
          aFilter.addMappingForUrlPatterns (null, false, "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_AJAX))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_AJAX, PhotonAjaxServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.addMapping (PhotonAjaxServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_API))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_API, PhotonAPIServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.addMapping (PhotonAPIServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_STREAM))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_STREAM, StreamServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.setInitParameter (AbstractObjectDeliveryHttpHandler.INITPARAM_ALLOWED_EXTENSIONS,
                                     AbstractObjectDeliveryHttpHandler.EXTENSION_MACRO_WEB_DEFAULT);
          aServlet.addMapping (StreamServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_USER_STREAM))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_USER_STREAM, UserStreamServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.setInitParameter (AbstractObjectDeliveryHttpHandler.INITPARAM_ALLOWED_EXTENSIONS,
                                     AbstractObjectDeliveryHttpHandler.EXTENSION_MACRO_WEB_DEFAULT);
          aServlet.addMapping (UserStreamServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_USER_UPLOAD))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_USER_UPLOAD, UserUploadServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.addMapping (UserUploadServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_LOGOUT))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_LOGOUT, LogoutServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.addMapping (LogoutServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_RESOURCE_BUNDLE))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_RESOURCE_BUNDLE,
                                                                     ResourceBundleServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.setInitParameter (AbstractObjectDeliveryHttpHandler.INITPARAM_ALLOWED_EXTENSIONS, "js,css");
          aServlet.addMapping (ResourceBundleServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      if (_isServletEnabled (aDisabledServletNames, SERVLET_NAME_GO))
      {
        final ServletRegistration.Dynamic aServlet = aSC.addServlet (SERVLET_NAME_GO, GoServlet.class);
        if (aServlet != null)
        {
          aServlet.setAsyncSupported (true);
          aServlet.addMapping (GoServlet.SERVLET_DEFAULT_PATH + "/*");
        }
      }

      {
        aSC.addListener (PhotonStubServletContextListener.class);
      }

      LOGGER.info ("Finished registering default ph-oton listeners and servlets");
    }
  }
}
