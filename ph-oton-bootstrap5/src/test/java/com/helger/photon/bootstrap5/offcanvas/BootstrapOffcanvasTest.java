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
package com.helger.photon.bootstrap5.offcanvas;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.photon.bootstrap5.button.BootstrapButton;

/**
 * Test class for class {@link BootstrapOffcanvas}.
 *
 * @author Philip Helger
 */
public final class BootstrapOffcanvasTest
{
  @Test
  public void testHeaderAndBody ()
  {
    final BootstrapOffcanvas aOC = new BootstrapOffcanvas (EBootstrapOffcanvasPlacement.END);
    aOC.setHeader ("Menu").setBody ("Content");
    final String sID = aOC.getID ();
    assertEquals ("<div id=\"" +
                  sID +
                  "\" class=\"offcanvas offcanvas-end\" tabindex=\"-1\" aria-labelledby=\"" +
                  sID +
                  "title\">" +
                  "<div class=\"offcanvas-header\">" +
                  "<h5 id=\"" +
                  sID +
                  "title\" class=\"offcanvas-title\">Menu</h5>" +
                  "<button class=\"btn-close\" aria-label=\"Close\" data-bs-dismiss=\"offcanvas\" type=\"button\"></button>" +
                  "</div>" +
                  "<div class=\"offcanvas-body\">Content</div>" +
                  "</div>",
                  getAsHTMLString (aOC));
  }

  @Test
  public void testPlacements ()
  {
    for (final EBootstrapOffcanvasPlacement ePlacement : EBootstrapOffcanvasPlacement.values ())
    {
      final BootstrapOffcanvas aOC = new BootstrapOffcanvas (ePlacement);
      aOC.setBody ("Content");
      final String sHTML = getAsHTMLString (aOC);
      assertTrue (sHTML, sHTML.contains ("class=\"offcanvas " + ePlacement.getCSSClass () + "\""));
    }
  }

  @Test
  public void testNoClose ()
  {
    final BootstrapOffcanvas aOC = new BootstrapOffcanvas ();
    aOC.setHeader ("Menu").setShowClose (false);
    final String sHTML = getAsHTMLString (aOC);
    assertTrue (sHTML, !sHTML.contains ("btn-close"));
  }

  @Test
  public void testApplyToggleTo ()
  {
    final BootstrapOffcanvas aOC = new BootstrapOffcanvas ();
    final BootstrapButton aButton = new BootstrapButton ().addChild ("Open");
    aOC.applyToggleTo (aButton);
    final String sHTML = getAsHTMLString (aButton);
    // Bootstrap 5 uses the "data-bs-" prefix
    assertTrue (sHTML, sHTML.contains ("data-bs-toggle=\"offcanvas\""));
    assertTrue (sHTML, sHTML.contains ("data-bs-target=\"#" + aOC.getID () + "\""));
    assertTrue (sHTML, sHTML.contains ("aria-controls=\"" + aOC.getID () + "\""));
  }

  @Test
  public void testBackdropAndScroll ()
  {
    final BootstrapOffcanvas aOC = new BootstrapOffcanvas ();
    aOC.setBody ("Content").setBackdrop ("static").setBodyScroll (true);
    final String sHTML = getAsHTMLString (aOC);
    assertTrue (sHTML, sHTML.contains ("data-bs-backdrop=\"static\""));
    assertTrue (sHTML, sHTML.contains ("data-bs-scroll=\"true\""));
  }
}
