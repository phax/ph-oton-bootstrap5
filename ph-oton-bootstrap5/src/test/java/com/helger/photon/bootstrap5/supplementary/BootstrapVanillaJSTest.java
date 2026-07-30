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
package com.helger.photon.bootstrap5.supplementary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.html.jquery.JQuerySelector;
import com.helger.photon.bootstrap5.modal.BootstrapModal;
import com.helger.photon.bootstrap5.modal.EBootstrapModalOptionBackdrop;
import com.helger.photon.bootstrap5.tooltip.BootstrapTooltip;

/**
 * Test that the Bootstrap 5 components create vanilla JS invocations and no jQuery plugin calls.
 *
 * @author Philip Helger
 */
public final class BootstrapVanillaJSTest
{
  @Test
  public void testTooltipAttach ()
  {
    final BootstrapTooltip aTooltip = new BootstrapTooltip (JQuerySelector.id ("foo")).setTooltipTitle ("Help");
    final String sJS = aTooltip.jsAttach ().getJSCode ();
    assertTrue (sJS, sJS.contains ("document.querySelectorAll('#foo')"));
    assertTrue (sJS, sJS.contains ("new bootstrap.Tooltip"));
    assertFalse (sJS, sJS.contains ("$("));

    final String sShowJS = aTooltip.jsShow ().getJSCode ();
    assertTrue (sShowJS, sShowJS.contains ("bootstrap.Tooltip.getOrCreateInstance"));
    assertTrue (sShowJS, sShowJS.contains (".show()"));
  }

  @Test
  public void testModal ()
  {
    final BootstrapModal aModal = new BootstrapModal ();
    final String sID = aModal.getID ();
    final String sShowJS = aModal.jsModalShow ().getJSCode ();
    assertTrue (sShowJS,
                sShowJS.contains ("bootstrap.Modal.getOrCreateInstance(document.getElementById('" +
                                  sID +
                                  "')).show()"));

    final String sOpenJS = aModal.openModal (EBootstrapModalOptionBackdrop.STATIC, Boolean.TRUE, null).getJSCode ();
    assertTrue (sOpenJS, sOpenJS.contains ("new bootstrap.Modal(document.getElementById('" + sID + "')"));
    assertTrue (sOpenJS, sOpenJS.contains ("backdrop:'static'"));
    assertTrue (sOpenJS, sOpenJS.contains (".show()"));
    assertFalse (sOpenJS, sOpenJS.contains ("$("));
  }
}
