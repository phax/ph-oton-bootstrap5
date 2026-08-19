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
package com.helger.photon.bootstrap5.navbar;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapNavbar}.
 *
 * @author Philip Helger
 */
public final class BootstrapNavbarTest
{
  @Test
  public void testExpandTypes ()
  {
    for (final EBootstrapNavbarExpandType eExpand : EBootstrapNavbarExpandType.values ())
    {
      final BootstrapNavbar aNavbar = new BootstrapNavbar ();
      aNavbar.setExpand (eExpand);
      aNavbar.addAndReturnText ().addChild ("Text");
      final String sHTML = getAsHTMLString (aNavbar);
      assertTrue (sHTML, sHTML.contains (eExpand.getCSSClass ()));
    }
  }

  @Test
  public void testExpandXXL ()
  {
    // XXL is the Bootstrap 5 specific breakpoint
    assertEquals ("navbar-expand-xxl", EBootstrapNavbarExpandType.EXPAND_XXL.getCSSClass ());
  }

  @Test
  public void testToggler ()
  {
    final BootstrapNavbar aNavbar = new BootstrapNavbar ();
    aNavbar.addToggler ("target1");
    final String sHTML = getAsHTMLString (aNavbar);
    assertTrue (sHTML, sHTML.contains ("class=\"navbar-toggler\""));
    assertTrue (sHTML, sHTML.contains ("data-bs-toggle=\"collapse\""));
    assertTrue (sHTML, sHTML.contains ("data-bs-target=\"#target1\""));
    assertTrue (sHTML, sHTML.contains ("aria-controls=\"target1\""));
    assertTrue (sHTML, sHTML.contains ("<span class=\"navbar-toggler-icon\"></span>"));
  }
}
