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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.collection.commons.CommonsLinkedHashSet;
import com.helger.servlet.mock.MockServletContext;

/**
 * Test class for class {@link PhotonStubServletInitializer}.
 *
 * @author Philip Helger
 */
public final class PhotonStubServletInitializerTest
{
  @Test
  public void testGetDisabledServletNamesNone ()
  {
    final MockServletContext aSC = MockServletContext.create ();
    try
    {
      assertTrue (PhotonStubServletInitializer.getDisabledServletNames (aSC).isEmpty ());
    }
    finally
    {
      aSC.invalidate ();
    }
  }

  @Test
  public void testGetDisabledServletNames ()
  {
    // Whitespace and empty parts are ignored, unknown names are kept
    final String sValue = " " +
                          PhotonStubServletInitializer.SERVLET_NAME_LOGOUT +
                          ",," +
                          PhotonStubServletInitializer.SERVLET_NAME_AJAX +
                          " , Unknown";
    final MockServletContext aSC = MockServletContext.create ();
    aSC.addInitParameter (PhotonStubServletInitializer.INIT_PARAMETER_DISABLED_SERVLETS, sValue);
    try
    {
      assertEquals (new CommonsLinkedHashSet <> (PhotonStubServletInitializer.SERVLET_NAME_LOGOUT,
                                                 PhotonStubServletInitializer.SERVLET_NAME_AJAX,
                                                 "Unknown"),
                    PhotonStubServletInitializer.getDisabledServletNames (aSC));
    }
    finally
    {
      aSC.invalidate ();
    }
  }
}
