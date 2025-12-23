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
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.id.IHasID;
import com.helger.html.hc.IHCNode;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapForm;
import com.helger.photon.bootstrap5.traits.IHCBootstrap5Trait;
import com.helger.photon.uicore.page.AbstractWebPageForm;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.text.IMultilingualText;

/**
 * Abstract base class for a Bootstrap based web page that has the common form handling, with a list
 * view, details view, create and edit + binding.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type of the object to be handled.
 * @param <WPECTYPE>
 *        Web page execution context type
 */
@NotThreadSafe
public abstract class AbstractBootstrapWebPageForm <DATATYPE extends IHasID <String>, WPECTYPE extends IWebPageExecutionContext>
                                                   extends
                                                   AbstractWebPageForm <DATATYPE, WPECTYPE, BootstrapForm, BootstrapButtonToolbar>
                                                   implements
                                                   IHCBootstrap5Trait
{
  public AbstractBootstrapWebPageForm (@NonNull @Nonempty final String sID, @NonNull final String sName)
  {
    super (sID, getAsMLT (sName), null, BootstrapWebPageUIHandler.INSTANCE);
  }

  public AbstractBootstrapWebPageForm (@NonNull @Nonempty final String sID, @NonNull final IMultilingualText aName)
  {
    super (sID, aName, null, BootstrapWebPageUIHandler.INSTANCE);
  }

  public AbstractBootstrapWebPageForm (@NonNull @Nonempty final String sID,
                                       @NonNull final String sName,
                                       @Nullable final String sDescription)
  {
    super (sID, getAsMLT (sName), getAsMLT (sDescription), BootstrapWebPageUIHandler.INSTANCE);
  }

  public AbstractBootstrapWebPageForm (@NonNull @Nonempty final String sID,
                                       @NonNull final IMultilingualText aName,
                                       @Nullable final IMultilingualText aDescription)
  {
    super (sID, aName, aDescription, BootstrapWebPageUIHandler.INSTANCE);
  }

  @Override
  @Nullable
  @OverrideOnDemand
  public IHCNode getHeaderNode (@NonNull final WPECTYPE aWPEC)
  {
    final String sHeaderText = getHeaderText (aWPEC);
    return getUIHandler ().createPageHeader (sHeaderText);
  }

  @Override
  protected void modifyFormBeforeShowInputForm (@NonNull final WPECTYPE aWPEC,
                                                @NonNull final BootstrapForm aForm,
                                                final boolean bIsFormSubmitted)
  {
    // That class is only suitable when using JS only validation, because it
    // uses the ":valid" and ":invalid" CSS pseudoclasses - as in
    // https://css-tricks.com/almanac/selectors/v/valid
    if (false)
      if (bIsFormSubmitted)
        aForm.addClass (CBootstrapCSS.WAS_VALIDATED);
  }
}
