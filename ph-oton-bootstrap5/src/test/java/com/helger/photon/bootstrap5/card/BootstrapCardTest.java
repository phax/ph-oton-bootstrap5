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
package com.helger.photon.bootstrap5.card;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapCard}.
 *
 * @author Philip Helger
 */
public final class BootstrapCardTest
{
  @Test
  public void testAllParts ()
  {
    final BootstrapCard aCard = new BootstrapCard ();
    aCard.createAndAddHeader ().addChild ("H");
    aCard.createAndAddBody ().addChild ("B");
    aCard.createAndAddFooter ().addChild ("F");
    assertEquals ("<div class=\"card\">" +
                  "<div class=\"card-header\">H</div>" +
                  "<div class=\"card-body\">B</div>" +
                  "<div class=\"card-footer\">F</div>" +
                  "</div>",
                  getAsHTMLString (aCard));
  }

  @Test
  public void testBodyOnly ()
  {
    final BootstrapCard aCard = new BootstrapCard ();
    aCard.createAndAddBody ().addChild ("B");
    assertEquals ("<div class=\"card\"><div class=\"card-body\">B</div></div>", getAsHTMLString (aCard));
  }
}
