/*
 * Copyright (C) 2018-2025 Philip Helger (www.helger.com)
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
package com.helger.photon.bootstrap5.pages;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.html.hc.IHCNode;
import com.helger.photon.bootstrap5.traits.IHCBootstrap5Trait;
import com.helger.photon.uicore.page.AbstractWebPage;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.text.IMultilingualText;

public abstract class AbstractBootstrapWebPage <WPECTYPE extends IWebPageExecutionContext> extends
                                               AbstractWebPage <WPECTYPE> implements
                                               IHCBootstrap5Trait
{
  public AbstractBootstrapWebPage (@NonNull @Nonempty final String sID, @NonNull final String sName)
  {
    super (sID, getAsMLT (sName), null);
  }

  public AbstractBootstrapWebPage (@NonNull @Nonempty final String sID, @NonNull final IMultilingualText aName)
  {
    super (sID, aName, null);
  }

  public AbstractBootstrapWebPage (@NonNull @Nonempty final String sID,
                                   @NonNull final String sName,
                                   @Nullable final String sDescription)
  {
    super (sID, getAsMLT (sName), getAsMLT (sDescription));
  }

  public AbstractBootstrapWebPage (@NonNull @Nonempty final String sID,
                                   @NonNull final IMultilingualText aName,
                                   @Nullable final IMultilingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @NonNull
  protected BootstrapWebPageUIHandler getUIHandler ()
  {
    return BootstrapWebPageUIHandler.INSTANCE;
  }

  @Override
  @Nullable
  @OverrideOnDemand
  public IHCNode getHeaderNode (@NonNull final WPECTYPE aWPEC)
  {
    final String sHeaderText = getHeaderText (aWPEC);
    return getUIHandler ().createPageHeader (sHeaderText);
  }
}
