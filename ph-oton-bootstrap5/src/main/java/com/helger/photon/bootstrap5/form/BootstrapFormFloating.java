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
package com.helger.photon.bootstrap5.form;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.hc.html.forms.IHCControl;
import com.helger.html.hc.html.forms.IHCInput;
import com.helger.html.hc.html.forms.IHCTextArea;
import com.helger.html.hc.html.forms.HCLabel;
import com.helger.photon.bootstrap5.CBootstrapCSS;
import com.helger.photon.bootstrap5.base.AbstractBootstrapDiv;

/**
 * Bootstrap 5 floating label wrapper (<code>form-floating</code>). The control comes first, the
 * label afterwards. The control requires a placeholder to be present for the CSS to work.
 *
 * @author Philip Helger
 */
public class BootstrapFormFloating extends AbstractBootstrapDiv <BootstrapFormFloating>
{
  public BootstrapFormFloating (@NonNull final IHCControl <?> aCtrl, @Nullable final String sLabel)
  {
    ValueEnforcer.notNull (aCtrl, "Ctrl");
    addClass (CBootstrapCSS.FORM_FLOATING);

    aCtrl.ensureID ();
    // A placeholder must be present for the floating CSS to work
    if (aCtrl instanceof final IHCInput <?> aInput)
    {
      if (!aInput.hasPlaceholder ())
        aInput.setPlaceholder (sLabel != null ? sLabel : "");
    }
    else
      if (aCtrl instanceof final IHCTextArea <?> aTextArea)
      {
        if (!aTextArea.hasPlaceholder ())
          aTextArea.setPlaceholder (sLabel != null ? sLabel : "");
      }
    BootstrapFormHelper.markAsFormControl (aCtrl);
    addChild (aCtrl);

    if (sLabel != null)
      addChild (new HCLabel ().setFor (aCtrl.getID ()).addChild (sLabel));
  }
}
