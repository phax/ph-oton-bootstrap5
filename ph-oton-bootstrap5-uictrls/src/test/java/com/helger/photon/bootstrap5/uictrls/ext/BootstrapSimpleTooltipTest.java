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
package com.helger.photon.bootstrap5.uictrls.ext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Rule;
import org.junit.Test;

import com.helger.html.hc.IHCNode;
import com.helger.html.hc.config.HCConversionSettings;
import com.helger.html.hc.config.HCSettings;
import com.helger.html.hc.render.HCRenderer;
import com.helger.photon.app.mock.PhotonAppWebTestRule;

/**
 * Test class for class {@link BootstrapSimpleTooltip}.
 *
 * @author Philip Helger
 */
public final class BootstrapSimpleTooltipTest
{
  @Rule
  public final PhotonAppWebTestRule m_aRule = new PhotonAppWebTestRule ();

  @NonNull
  private static String _getAsHTMLString (@NonNull final IHCNode aNode)
  {
    final HCConversionSettings aCS = HCSettings.getConversionSettingsWithoutNamespaces ().getClone ();
    aCS.setXMLWriterSettingsOptimized (true);
    return HCRenderer.getAsHTMLString (aNode, aCS);
  }

  @Test
  public void testCreateSimpleTooltip ()
  {
    final String sHTML = _getAsHTMLString (BootstrapSimpleTooltip.createSimpleTooltip ("Some help"));
    // The tooltip must be attached with the native Bootstrap 5 JS API and not with the
    // jQuery plugin API that was removed in Bootstrap 5. The surrounding "document ready"
    // code comes from the ph-oton IHCOnDocumentReadyProvider and is independent of Bootstrap.
    assertTrue (sHTML, sHTML.contains ("new bootstrap.Tooltip"));
    assertTrue (sHTML, sHTML.contains ("document.querySelectorAll"));
    assertTrue (sHTML, sHTML.contains ("Some help"));
    assertFalse (sHTML, sHTML.contains (".tooltip("));
  }
}
