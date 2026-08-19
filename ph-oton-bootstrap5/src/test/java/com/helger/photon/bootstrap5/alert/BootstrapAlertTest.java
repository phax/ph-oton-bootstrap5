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
package com.helger.photon.bootstrap5.alert;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for the {@link AbstractBootstrapAlert} implementations.
 *
 * @author Philip Helger
 */
public final class BootstrapAlertTest
{
  @Test
  public void testTypes ()
  {
    assertEquals ("<div class=\"alert alert-danger\" role=\"alert\">Bad</div>",
                  getAsHTMLString (new BootstrapErrorBox ().addChild ("Bad")));
    assertEquals ("<div class=\"alert alert-success\" role=\"alert\">Good</div>",
                  getAsHTMLString (new BootstrapSuccessBox ().addChild ("Good")));
    assertEquals ("<div class=\"alert alert-info\" role=\"alert\">FYI</div>",
                  getAsHTMLString (new BootstrapInfoBox ().addChild ("FYI")));
    assertEquals ("<div class=\"alert alert-warning\" role=\"alert\">Hmm</div>",
                  getAsHTMLString (new BootstrapWarnBox ().addChild ("Hmm")));
  }

  @Test
  public void testShowClose ()
  {
    // Bootstrap 5 uses "btn-close" and "data-bs-dismiss"
    assertEquals ("<div class=\"alert alert-success alert-dismissible\" role=\"alert\">OK" +
                  "<button class=\"btn-close\" aria-label=\"Close\" data-bs-dismiss=\"alert\" type=\"button\"></button>" +
                  "</div>",
                  getAsHTMLString (new BootstrapSuccessBox ().setShowClose (true).addChild ("OK")));
  }

  @Test
  public void testEmptyAlertIsNotRendered ()
  {
    assertEquals ("", getAsHTMLString (new BootstrapErrorBox ()));
  }

  @Test
  public void testSetTypeIfWorse ()
  {
    // "Worse" is determined by the declaration order of EBootstrapAlertType
    final BootstrapBox aBox = new BootstrapBox (EBootstrapAlertType.SUCCESS);
    aBox.setTypeIfWorse (EBootstrapAlertType.PRIMARY);
    assertEquals (EBootstrapAlertType.SUCCESS, aBox.getType ());
    aBox.setTypeIfWorse (EBootstrapAlertType.DANGER);
    assertEquals (EBootstrapAlertType.DANGER, aBox.getType ());
    aBox.setTypeIfWorse (EBootstrapAlertType.SECONDARY);
    assertEquals (EBootstrapAlertType.DANGER, aBox.getType ());
  }
}
