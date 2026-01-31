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
package com.helger.photon.bootstrap5.breadcrumb;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.grouping.AbstractHCOLBase;
import com.helger.html.hc.html.textlevel.HCA;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.url.ISimpleURL;

/**
 * Breadcrumb list. The only member of {@link BootstrapBreadcrumb}.
 *
 * @author Philip Helger
 */
public class BootstrapBreadcrumbList extends AbstractHCOLBase <BootstrapBreadcrumbList, BootstrapBreadcrumbItem>
{
  public BootstrapBreadcrumbList ()
  {
    super (BootstrapBreadcrumbItem.class);
  }

  @Override
  protected final BootstrapBreadcrumbItem createEmptyItem ()
  {
    return new BootstrapBreadcrumbItem ();
  }

  @NonNull
  public BootstrapBreadcrumbList addLink (@NonNull final ISimpleURL aURL, @Nullable final String sLabel)
  {
    return addLink (aURL, HCTextNode.createOnDemand (sLabel));
  }

  @NonNull
  public BootstrapBreadcrumbList addLink (@NonNull final ISimpleURL aURL, @Nullable final IHCNode aLabel)
  {
    return addLink (new HCA ().setHref (aURL).addChild (aLabel));
  }

  @NonNull
  public BootstrapBreadcrumbList addLink (@NonNull final HCA aLink)
  {
    addChild (new BootstrapBreadcrumbItem ().addChild (aLink));
    return this;
  }

  @NonNull
  public BootstrapBreadcrumbList addActive (@Nullable final String sLabel)
  {
    return addActive (HCTextNode.createOnDemand (sLabel));
  }

  @NonNull
  public BootstrapBreadcrumbList addActive (@Nullable final IHCNode aLabel)
  {
    addChild (new BootstrapBreadcrumbItem ().addChild (aLabel).setActive (true));
    return this;
  }

  @Override
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClass (CBootstrapCSS.BREADCRUMB);
  }
}
