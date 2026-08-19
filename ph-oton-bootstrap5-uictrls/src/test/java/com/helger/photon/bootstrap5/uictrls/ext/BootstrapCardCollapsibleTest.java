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

import static org.junit.Assert.assertTrue;

import org.jspecify.annotations.NonNull;
import org.junit.Rule;
import org.junit.Test;

import com.helger.html.hc.IHCNode;
import com.helger.html.hc.config.HCConversionSettings;
import com.helger.html.hc.config.HCSettings;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.html.hc.render.HCRenderer;
import com.helger.photon.app.mock.PhotonAppWebTestRule;

/**
 * Test class for class {@link BootstrapCardCollapsible}.
 *
 * @author Philip Helger
 */
public final class BootstrapCardCollapsibleTest
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
  public void testOpen ()
  {
    final BootstrapCardCollapsible aCard = new BootstrapCardCollapsible (new HCTextNode ("Header"), true);
    aCard.getBody ().addChild ("Body");
    final String sHTML = _getAsHTMLString (aCard);
    assertTrue (sHTML, sHTML.contains ("class=\"card card-collapsible\""));
    assertTrue (sHTML, sHTML.contains ("card-header"));
    assertTrue (sHTML, sHTML.contains ("class=\"collapse show\""));
    // Bootstrap 5 uses the "data-bs-" prefix
    assertTrue (sHTML, sHTML.contains ("data-bs-toggle=\"collapse\""));
    assertTrue (sHTML, sHTML.contains ("aria-expanded=\"true\""));
    assertTrue (sHTML, sHTML.contains ("aria-controls=\"" + aCard.getCollapseDiv ().getID () + "\""));
    assertTrue (sHTML, sHTML.contains ("card-body"));
  }

  @Test
  public void testClosed ()
  {
    final BootstrapCardCollapsible aCard = new BootstrapCardCollapsible (new HCTextNode ("Header"), false);
    aCard.getBody ().addChild ("Body");
    final String sHTML = _getAsHTMLString (aCard);
    assertTrue (sHTML, sHTML.contains ("class=\"collapse\""));
    assertTrue (sHTML, sHTML.contains ("aria-expanded=\"false\""));
    assertTrue (sHTML, sHTML.contains ("collapsed"));
  }
}
