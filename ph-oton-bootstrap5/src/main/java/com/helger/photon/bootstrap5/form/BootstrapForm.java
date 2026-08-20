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

import java.util.Locale;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.html.EHTMLRole;
import com.helger.html.hc.IHCConversionSettingsToNode;
import com.helger.html.hc.IHCHasChildrenMutable;
import com.helger.html.hc.IHCNode;
import com.helger.html.hc.html.IHCElementWithChildren;
import com.helger.html.hc.html.forms.AbstractHCForm;
import com.helger.photon.bootstrap5.grid.BootstrapGridSpec;
import com.helger.photon.core.execcontext.ISimpleWebExecutionContext;

@NotThreadSafe
public class BootstrapForm extends AbstractHCForm <BootstrapForm> implements
                           IBootstrapFormGroupContainer <BootstrapForm>
{
  private final Locale m_aDisplayLocale;
  private EBootstrapFormType m_eFormType = EBootstrapFormType.DEFAULT;
  private BootstrapGridSpec m_aLeftGrid = BootstrapFormSettings.getDefaultLeftGrid ();
  private BootstrapGridSpec m_aRightGrid = BootstrapFormSettings.getDefaultRightGrid ();
  private IBootstrapFormGroupRenderer m_aFormGroupRenderer = new DefaultBootstrapFormGroupRenderer ();

  public BootstrapForm (@NonNull final ISimpleWebExecutionContext aLEC)
  {
    // Not needed, as the role is identical to the HTML semantics
    if (false)
      setRole (EHTMLRole.FORM);
    m_aDisplayLocale = aLEC.getDisplayLocale ();
  }

  @NonNull
  public final EBootstrapFormType getFormType ()
  {
    return m_eFormType;
  }

  /**
   * @param eFormType
   *        The form type to be used. May not be <code>null</code>.
   * @return this
   */
  @NonNull
  public final BootstrapForm setFormType (@NonNull final EBootstrapFormType eFormType)
  {
    ValueEnforcer.notNull (eFormType, "FormType");
    m_eFormType = eFormType;
    return this;
  }

  @NonNull
  public final BootstrapGridSpec getLeft ()
  {
    return m_aLeftGrid;
  }

  @NonNull
  public final BootstrapGridSpec getRight ()
  {
    return m_aRightGrid;
  }

  @NonNull
  @OverridingMethodsMustInvokeSuper
  public BootstrapForm setLeft (@NonNull final BootstrapGridSpec aNewLeft)
  {
    // The right side is the complement of the left side
    return setSplitting (aNewLeft, aNewLeft.getInverse ());
  }

  @NonNull
  @OverridingMethodsMustInvokeSuper
  public BootstrapForm setSplitting (@NonNull final BootstrapGridSpec aLeft, @NonNull final BootstrapGridSpec aRight)
  {
    ValueEnforcer.notNull (aLeft, "Left");
    ValueEnforcer.notNull (aRight, "Right");
    m_aLeftGrid = aLeft;
    m_aRightGrid = aRight;
    return this;
  }

  @NonNull
  public IBootstrapFormGroupRenderer getFormGroupRenderer ()
  {
    return m_aFormGroupRenderer;
  }

  @NonNull
  public BootstrapForm setFormGroupRenderer (@NonNull final IBootstrapFormGroupRenderer aFormGroupRenderer)
  {
    m_aFormGroupRenderer = ValueEnforcer.notNull (aFormGroupRenderer, "FormGroupRenderer");
    return this;
  }

  @NonNull
  public IHCElementWithChildren <?> getRenderedFormGroup (@NonNull final BootstrapFormGroup aFormGroup)
  {
    return m_aFormGroupRenderer.renderFormGroup (this, aFormGroup, m_aDisplayLocale);
  }

  @NonNull
  public BootstrapForm addFormGroup (@NonNull final BootstrapFormGroup aFormGroup)
  {
    // Must be added directly and cannot be added via a proxy, because
    // otherwise, the adding may happen after the out of band nodes were
    // extracted!
    return addChild (getRenderedFormGroup (aFormGroup));
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void onFinalizeNodeState (@NonNull final IHCConversionSettingsToNode aConversionSettings,
                                      @NonNull final IHCHasChildrenMutable <?, ? super IHCNode> aTargetNode)
  {
    super.onFinalizeNodeState (aConversionSettings, aTargetNode);
    addClass (m_eFormType);
  }
}
