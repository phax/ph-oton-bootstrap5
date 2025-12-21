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
package com.helger.photon.bootstrap5.form;

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.string.StringHelper;
import com.helger.diagnostics.error.list.IErrorList;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.ext.HCExtHelper;
import com.helger.html.hc.html.IHCElementWithChildren;
import com.helger.html.hc.html.grouping.HCDiv;
import com.helger.html.hc.impl.HCNodeList;
import com.helger.html.hc.impl.HCTextNode;
import com.helger.photon.bootstrap5.base.AbstractBootstrapObject;
import com.helger.photon.core.EPhotonCoreText;
import com.helger.photon.uicore.html.formlabel.ELabelType;
import com.helger.photon.uicore.html.formlabel.HCFormLabel;

/**
 * Represents a single form group.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class BootstrapFormGroup extends AbstractBootstrapObject <BootstrapFormGroup>
{
  private HCFormLabel m_aLabel;
  private IHCNode m_aCtrl;
  private HCNodeList m_aHelpText;
  private IErrorList m_aErrorList;

  public BootstrapFormGroup ()
  {}

  @NonNull
  public final BootstrapFormGroup setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createOptional (sLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabel (@Nullable final IHCElementWithChildren <?> aLabel)
  {
    return setLabel (aLabel == null ? null : HCFormLabel.createOptional (aLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabelMandatory (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createMandatory (sLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabelMandatory (@Nullable final IHCElementWithChildren <?> aLabel)
  {
    return setLabel (aLabel == null ? null : HCFormLabel.createMandatory (aLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabelAlternative (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createAlternative (sLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabelAlternative (@Nullable final IHCElementWithChildren <?> aLabel)
  {
    return setLabel (aLabel == null ? null : HCFormLabel.createAlternative (aLabel));
  }

  @NonNull
  public final BootstrapFormGroup setLabel (@Nullable final String sLabel, @NonNull final ELabelType eLabelType)
  {
    return setLabel (sLabel == null ? null : new HCFormLabel (sLabel, eLabelType));
  }

  @NonNull
  public final BootstrapFormGroup setLabelForCheckBox (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createForCheckBox (sLabel));
  }

  /**
   * Called after the label cell was altered.
   *
   * @param aLabel
   *        The newly set label. May be <code>null</code>
   */
  @OverrideOnDemand
  protected void onLabelModified (@Nullable final HCFormLabel aLabel)
  {}

  @NonNull
  public final BootstrapFormGroup setLabel (@Nullable final HCFormLabel aLabel)
  {
    m_aLabel = aLabel;
    onLabelModified (aLabel);
    return this;
  }

  @Nullable
  public final HCFormLabel getLabel ()
  {
    return m_aLabel;
  }

  /**
   * Called after the control was changed
   *
   * @param aCtrl
   *        The new control. May be <code>null</code>.
   */
  @OverrideOnDemand
  protected void onCtrlModified (@Nullable final IHCNode aCtrl)
  {}

  @NonNull
  public final BootstrapFormGroup setCtrl (@Nullable final String sValue)
  {
    if (StringHelper.isEmpty (sValue))
      return this;

    return setCtrl (new HCDiv ().addChild (sValue));
  }

  @NonNull
  public final BootstrapFormGroup setCtrl (@Nullable final String... aValues)
  {
    final HCNodeList aNodeList = new HCNodeList ();
    if (aValues != null)
      for (final String sValue : aValues)
        aNodeList.addChild (new HCDiv ().addChild (sValue));
    return setCtrl (aNodeList);
  }

  @NonNull
  public final BootstrapFormGroup setCtrl (final boolean bYesOrNo, @NonNull final Locale aDisplayLocale)
  {
    return setCtrl (EPhotonCoreText.getYesOrNo (bYesOrNo, aDisplayLocale));
  }

  @NonNull
  public final BootstrapFormGroup setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrl = aCtrl;
    onCtrlModified (aCtrl);
    return this;
  }

  @NonNull
  public final BootstrapFormGroup setCtrl (@Nullable final IHCNode... aCtrls)
  {
    return setCtrl (new HCNodeList ().addChildren (aCtrls));
  }

  @NonNull
  public final BootstrapFormGroup setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    return setCtrl (new HCNodeList ().addChildren (aCtrls));
  }

  @Nullable
  public final IHCNode getCtrl ()
  {
    return m_aCtrl;
  }

  @NonNull
  public final BootstrapFormGroup setErrorList (@Nullable final IErrorList aErrorList)
  {
    m_aErrorList = aErrorList;
    return this;
  }

  @Nullable
  public final IErrorList getErrorList ()
  {
    return m_aErrorList;
  }

  /**
   * Called after the help text was altered.
   *
   * @param aNote
   *        The new help text node. May be <code>null</code>.
   */
  @OverrideOnDemand
  protected void onHelpTextModified (@Nullable final IHCNode aNote)
  {}

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final String sHelpText)
  {
    // Use <br> to avoid <div> in parent <span>
    return setHelpText (new HCNodeList ().addChildren (HCExtHelper.nl2brList (sHelpText)));
  }

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final HCNodeList aHelpText)
  {
    m_aHelpText = aHelpText;
    onHelpTextModified (aHelpText);
    return this;
  }

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final IHCNode aHelpText)
  {
    return setHelpText (aHelpText instanceof HCNodeList ? (HCNodeList) aHelpText : new HCNodeList ().addChild (
                                                                                                               aHelpText));
  }

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final String... aHelpTexts)
  {
    return setHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final IHCNode... aHelpTexts)
  {
    return setHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @NonNull
  public final BootstrapFormGroup setHelpText (@Nullable final Iterable <? extends IHCNode> aHelpTexts)
  {
    return setHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @NonNull
  public final BootstrapFormGroup addHelpText (@Nullable final String sHelpText)
  {
    return addHelpText (HCTextNode.createOnDemand (sHelpText));
  }

  @NonNull
  public final BootstrapFormGroup addHelpText (@Nullable final IHCNode aHelpText)
  {
    if (aHelpText != null)
    {
      if (m_aHelpText == null)
        m_aHelpText = new HCNodeList ();
      m_aHelpText.addChild (aHelpText);
      onHelpTextModified (m_aHelpText);
    }
    return this;
  }

  @NonNull
  public final BootstrapFormGroup addHelpText (@Nullable final String... aHelpTexts)
  {
    return addHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @NonNull
  public final BootstrapFormGroup addHelpText (@Nullable final IHCNode... aHelpTexts)
  {
    return addHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @NonNull
  public final BootstrapFormGroup addHelpText (@Nullable final Iterable <? extends IHCNode> aHelpTexts)
  {
    return addHelpText (new HCNodeList ().addChildren (aHelpTexts));
  }

  @Nullable
  public final IHCNode getHelpText ()
  {
    return m_aHelpText;
  }
}
