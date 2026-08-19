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
package com.helger.photon.bootstrap5.dropdown;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapDropdownMenu}.
 *
 * @author Philip Helger
 */
public final class BootstrapDropdownMenuTest
{
  @Test
  public void testItemsAndDivider ()
  {
    final BootstrapDropdownMenu aDD = new BootstrapDropdownMenu ();
    aDD.createAndAddItem ().addChild ("Item");
    aDD.createAndAddDivider ();
    assertEquals ("<ul class=\"dropdown-menu\">" +
                  "<li><a class=\"dropdown-item\">Item</a></li>" +
                  "<li><hr class=\"dropdown-divider\" /></li>" +
                  "</ul>",
                  getAsHTMLString (aDD));
  }

  @Test
  public void testAlignEnd ()
  {
    final BootstrapDropdownMenu aDD = new BootstrapDropdownMenu ();
    aDD.setAlignEnd (true);
    aDD.createAndAddItem ().addChild ("Item");
    // Bootstrap 5 renamed "dropdown-menu-right" to "dropdown-menu-end"
    final String sHTML = getAsHTMLString (aDD);
    assertTrue (sHTML, sHTML.contains ("dropdown-menu-end"));
  }

  @Test
  public void testHeaderAndText ()
  {
    final BootstrapDropdownMenu aDD = new BootstrapDropdownMenu ();
    aDD.createAndAddHeader ().addChild ("Header");
    aDD.createAndAddText ("Text");
    final String sHTML = getAsHTMLString (aDD);
    assertTrue (sHTML, sHTML.contains ("dropdown-header"));
    assertTrue (sHTML, sHTML.contains ("dropdown-item-text"));
  }
}
