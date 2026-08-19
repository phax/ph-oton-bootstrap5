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
package com.helger.photon.bootstrap5.inputgroup;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.html.hc.html.forms.HCEdit;
import com.helger.photon.bootstrap5.button.BootstrapButton;

/**
 * Test class for class {@link BootstrapInputGroup}.
 *
 * @author Philip Helger
 */
public final class BootstrapInputGroupTest
{
  @Test
  public void testPrefixAndSuffix ()
  {
    final BootstrapInputGroup aIG = new BootstrapInputGroup ();
    aIG.addChildPrefix ("@").addChild (new HCEdit ("field")).addChildSuffix (".com");
    final String sHTML = getAsHTMLString (aIG);
    // Bootstrap 5 has a flat structure - no "input-group-prepend" / "input-group-append" wrappers
    assertEquals ("<div class=\"input-group\">" +
                  "<span class=\"input-group-text\">@</span>" +
                  "<input name=\"field\" type=\"text\" />" +
                  "<span class=\"input-group-text\">.com</span>" +
                  "</div>",
                  sHTML);
    assertFalse (sHTML, sHTML.contains ("input-group-prepend"));
    assertFalse (sHTML, sHTML.contains ("input-group-append"));
  }

  @Test
  public void testSize ()
  {
    final BootstrapInputGroup aIG = new BootstrapInputGroup (EBootstrapInputGroupSize.LARGE);
    aIG.addChild (new HCEdit ("field"));
    assertEquals ("<div class=\"input-group input-group-lg\"><input name=\"field\" type=\"text\" /></div>",
                  getAsHTMLString (aIG));
  }

  @Test
  public void testButtonIsNotWrapped ()
  {
    final BootstrapInputGroup aIG = new BootstrapInputGroup ();
    aIG.addChild (new HCEdit ("field")).addChildSuffix (new BootstrapButton ().addChild ("Go"));
    final String sHTML = getAsHTMLString (aIG);
    assertTrue (sHTML, sHTML.contains ("<button class=\"btn btn-outline-secondary\" type=\"button\">Go</button>"));
    assertFalse (sHTML, sHTML.contains ("<span class=\"input-group-text\"><button"));
  }

  @Test
  public void testHasPrefixesAndSuffixes ()
  {
    final BootstrapInputGroup aIG = new BootstrapInputGroup ();
    assertFalse (aIG.hasPrefixes ());
    assertFalse (aIG.hasSuffixes ());
    aIG.addChildPrefix ("@");
    aIG.addChildSuffix ("!");
    assertTrue (aIG.hasPrefixes ());
    assertTrue (aIG.hasSuffixes ());
  }

  @Test
  public void testEmptyPrefixIsIgnored ()
  {
    final BootstrapInputGroup aIG = new BootstrapInputGroup ();
    aIG.addChildPrefix ((String) null);
    aIG.addChildPrefix ("");
    aIG.addChildSuffix ((String) null);
    aIG.addChildSuffix ("");
    assertFalse (aIG.hasPrefixes ());
    assertFalse (aIG.hasSuffixes ());
  }
}
