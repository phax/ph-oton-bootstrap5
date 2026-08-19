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
package com.helger.photon.bootstrap5.modal;

import static com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link BootstrapModal}.
 *
 * @author Philip Helger
 */
public final class BootstrapModalTest
{
  @Test
  public void testDefault ()
  {
    final BootstrapModal aModal = new BootstrapModal ();
    final String sID = aModal.getID ();
    assertEquals ("<div id=\"" +
                  sID +
                  "\" class=\"modal fade\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">" +
                  "<div class=\"modal-dialog modal-dialog-centered\" role=\"document\">" +
                  "<div id=\"" +
                  sID +
                  "content\" class=\"modal-content\"></div>" +
                  "</div>" +
                  "</div>",
                  getAsHTMLString (aModal));
  }

  @Test
  public void testSizes ()
  {
    for (final EBootstrapModalSize eSize : EBootstrapModalSize.values ())
    {
      final BootstrapModal aModal = new BootstrapModal (eSize);
      final String sHTML = getAsHTMLString (aModal);
      if (eSize.getCSSClass () != null)
        assertTrue (sHTML, sHTML.contains (eSize.getCSSClass ()));
    }
  }
}
