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

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.base.string.StringHelper;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.alert.BootstrapErrorBox;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapForm;
import com.helger.photon.bootstrap5.form.EBootstrapFormType;
import com.helger.photon.bootstrap5.utils.BootstrapPageHeader;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.core.execcontext.ILayoutExecutionContext;
import com.helger.photon.uicore.page.IWebPageFormUIHandler;

public class BootstrapWebPageUIHandler implements IWebPageFormUIHandler <BootstrapForm, BootstrapButtonToolbar>
{
  public static final BootstrapWebPageUIHandler INSTANCE = new BootstrapWebPageUIHandler ();

  protected BootstrapWebPageUIHandler ()
  {}

  @Nullable
  public BootstrapPageHeader createPageHeader (@Nullable final String sHeaderText)
  {
    if (StringHelper.isEmpty (sHeaderText))
      return null;
    return new BootstrapPageHeader ().addChild (sHeaderText);
  }

  @Override
  @NonNull
  public BootstrapForm createFormSelf (@NonNull final ILayoutExecutionContext aLEC, final boolean bIsFormSubmitted)
  {
    final BootstrapForm ret = new BootstrapForm (aLEC).setFormType (EBootstrapFormType.DEFAULT)
                                                      .setAction (aLEC.getSelfHref ());
    if (false)
      if (bIsFormSubmitted)
        ret.addClass (CBootstrapCSS.WAS_VALIDATED);
    return ret;
  }

  @Override
  @NonNull
  public BootstrapForm createFormFileUploadSelf (@NonNull final ILayoutExecutionContext aLEC,
                                                 final boolean bIsFormSubmitted)
  {
    return createFormSelf (aLEC, bIsFormSubmitted).setEncTypeFileUpload ();
  }

  @Override
  @NonNull
  public final BootstrapButtonToolbar createToolbar (@NonNull final ILayoutExecutionContext aLEC)
  {
    return new BootstrapButtonToolbar (aLEC).setForBottom (true);
  }

  @Override
  @NonNull
  public final BootstrapErrorBox createErrorBox (@NonNull final ILayoutExecutionContext aLEC,
                                                 @Nullable final String sErrorMsg)
  {
    return new BootstrapErrorBox ().addChild (sErrorMsg);
  }

  @Override
  @NonNull
  public final BootstrapErrorBox createIncorrectInputBox (@NonNull final ILayoutExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    return new BootstrapErrorBox ().addChild (EPhotonCoreText.ERR_INCORRECT_INPUT.getDisplayText (aDisplayLocale));
  }
}
