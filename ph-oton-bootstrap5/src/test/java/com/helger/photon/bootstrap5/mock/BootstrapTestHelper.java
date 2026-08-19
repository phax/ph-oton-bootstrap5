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
package com.helger.photon.bootstrap5.mock;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.concurrent.Immutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.config.HCConversionSettings;
import com.helger.html.hc.config.HCSettings;
import com.helger.html.hc.render.HCRenderer;

/**
 * Helper class to render HC nodes to HTML strings in unit tests.
 *
 * @author Philip Helger
 */
@Immutable
public final class BootstrapTestHelper
{
  private BootstrapTestHelper ()
  {}

  /**
   * Render the provided node to an HTML string without indentation, so that the created markup can
   * be checked with simple "contains" assertions.
   *
   * @param aNode
   *        The node to be rendered. May not be <code>null</code>.
   * @return The rendered HTML and never <code>null</code>.
   */
  @NonNull
  public static String getAsHTMLString (@NonNull final IHCNode aNode)
  {
    final HCConversionSettings aCS = HCSettings.getConversionSettingsWithoutNamespaces ().getClone ();
    aCS.setXMLWriterSettingsOptimized (true);
    return HCRenderer.getAsHTMLString (aNode, aCS);
  }
}
