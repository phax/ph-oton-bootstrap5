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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.junit.Rule;
import org.junit.Test;

import com.helger.base.state.ETriState;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.config.HCConversionSettings;
import com.helger.html.hc.config.HCSettings;
import com.helger.html.hc.render.HCRenderer;
import com.helger.photon.app.mock.PhotonAppWebTestRule;

/**
 * Test class for class {@link BootstrapDateTimePicker}.<br>
 * These tests pin the contract between this wrapper and the shipped Tempus Dominus version - see
 * <code>src/main/resources/external/tempusdominus/README.md</code> before changing an expectation
 * here.
 *
 * @author Philip Helger
 */
public final class BootstrapDateTimePickerTest
{
  private static final Locale LOCALE = Locale.GERMANY;

  @Rule
  public final PhotonAppWebTestRule m_aRule = new PhotonAppWebTestRule ();

  @NonNull
  private static String _getAsHTMLString (@NonNull final IHCNode aNode)
  {
    final HCConversionSettings aCS = HCSettings.getConversionSettingsWithoutNamespaces ().getClone ();
    aCS.setXMLWriterSettingsOptimized (true);
    return HCRenderer.getAsHTMLString (aNode, aCS);
  }

  @NonNull
  private static BootstrapDateTimePicker _createDate ()
  {
    return BootstrapDateTimePicker.create ("dt", LocalDate.of (2026, Month.AUGUST, 19), LOCALE);
  }

  @Test
  public void testDOMContract ()
  {
    final BootstrapDateTimePicker aDTP = _createDate ();
    final String sHTML = _getAsHTMLString (aDTP);
    // The attributes Tempus Dominus queries for
    assertTrue (sHTML, sHTML.contains ("data-td-target-input=\"nearest\""));
    assertTrue (sHTML, sHTML.contains ("data-td-target-toggle=\"nearest\""));
    assertTrue (sHTML, sHTML.contains ("data-td-toggle=\"datetimepicker\""));
    assertTrue (sHTML, sHTML.contains ("data-td-target=\"#" + aDTP.getID () + "\""));
    assertTrue (sHTML, sHTML.contains ("datetimepicker-input"));
  }

  @Test
  public void testNoToggleAttributeWithoutIcon ()
  {
    // Without a prepend icon Tempus Dominus uses the whole input group as toggle - the
    // "data-td-target-toggle" attribute must not be present in that case
    final BootstrapDateTimePicker aDTP = _createDate ().setPrependIcon (null);
    final String sHTML = _getAsHTMLString (aDTP);
    assertTrue (sHTML, !sHTML.contains ("data-td-target-toggle"));
    assertTrue (sHTML, !sHTML.contains ("data-td-toggle"));
  }

  @Test
  public void testJSInitialization ()
  {
    final String sHTML = _getAsHTMLString (_createDate ());
    // Tempus Dominus 6 is initialized as a plain JS object - not as a jQuery plugin
    assertTrue (sHTML, sHTML.contains ("new tempusDominus.TempusDominus"));
    assertTrue (sHTML, !sHTML.contains (".datetimepicker("));
  }

  @Test
  public void testJSOptions ()
  {
    final String sOptions = _createDate ().getJSOptions ().getJSCode ();
    // Every key used here must exist in the Tempus Dominus option schema - an unknown option
    // makes Tempus Dominus throw a TdError at runtime
    for (final String sKey : new String [] { "display",
                                             "icons",
                                             "components",
                                             "buttons",
                                             "localization",
                                             "format",
                                             "locale",
                                             "defaultDate",
                                             "allowInputToggle" })
      assertTrue (sKey + " is missing in " + sOptions, sOptions.contains (sKey));
    // FontAwesome 5 icon classes - the Tempus Dominus defaults require FontAwesome 6
    assertTrue (sOptions, sOptions.contains ("fa fa-calendar"));
    assertTrue (sOptions, !sOptions.contains ("fa-solid"));
  }

  @Test
  public void testKeyboardNavigationOption ()
  {
    // Since Tempus Dominus 6.10 - not emitted as long as it is undefined
    assertTrue (!_createDate ().getJSOptions ().getJSCode ().contains ("keyboardNavigation"));
    final String sOptions = _createDate ().setKeyboardNavigation (ETriState.FALSE).getJSOptions ().getJSCode ();
    assertTrue (sOptions, sOptions.contains ("keyboardNavigation"));
  }

  @Test
  public void testGermanLocalization ()
  {
    final String sOptions = _createDate ().getJSOptions ().getJSCode ();
    assertTrue (sOptions, sOptions.contains ("de-DE"));
    // Since Tempus Dominus 6.10 - aria label of the toggle element
    assertTrue (sOptions, sOptions.contains ("toggleAriaLabel"));
  }
}
