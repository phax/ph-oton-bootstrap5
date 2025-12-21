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
package com.helger.photon.bootstrap5.buttongroup;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.EHTMLRole;
import com.helger.html.js.IHasJSCode;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.base.AbstractBootstrapDiv;
import com.helger.photon.bootstrap5.button.BootstrapButton;
import com.helger.photon.bootstrap5.button.BootstrapSubmitButton;
import com.helger.photon.core.execcontext.ILayoutExecutionContext;
import com.helger.photon.uicore.html.toolbar.IButtonToolbar;
import com.helger.photon.uicore.icon.IIcon;
import com.helger.url.ISimpleURL;
import com.helger.url.SimpleURL;

/**
 * Bootstrap3 button toolbar. Should only be used to group button groups and not
 * simple buttons.
 *
 * @author Philip Helger
 */
public class BootstrapButtonToolbar extends AbstractBootstrapDiv <BootstrapButtonToolbar> implements IButtonToolbar <BootstrapButtonToolbar>
{
  private final SimpleURL m_aSelfHref;

  public BootstrapButtonToolbar (@NonNull final ILayoutExecutionContext aLEC)
  {
    this (aLEC.getSelfHref ());
  }

  public BootstrapButtonToolbar (@NonNull final SimpleURL aSelfHref)
  {
    m_aSelfHref = ValueEnforcer.notNull (aSelfHref, "SelfHref");
    addClass (CBootstrapCSS.BTN_TOOLBAR);
    setRole (EHTMLRole.TOOLBAR);
  }

  @NonNull
  public ISimpleURL getSelfHref ()
  {
    return m_aSelfHref;
  }

  @NonNull
  public final BootstrapButton addAndReturnButton (@Nullable final String sCaption,
                                                   @Nullable final IHasJSCode aJSCode,
                                                   @Nullable final IIcon aIcon)
  {
    return addAndReturnChild (new BootstrapButton ().setIcon (aIcon).addChild (sCaption).setOnClick (aJSCode));
  }

  @NonNull
  public final BootstrapButton addAndReturnSubmitButton (@Nullable final String sCaption,
                                                         @Nullable final IHasJSCode aOnClick,
                                                         @Nullable final IIcon aIcon)
  {
    return addAndReturnChild (new BootstrapSubmitButton ().setIcon (aIcon).setOnClick (aOnClick).addChild (sCaption));
  }
}
