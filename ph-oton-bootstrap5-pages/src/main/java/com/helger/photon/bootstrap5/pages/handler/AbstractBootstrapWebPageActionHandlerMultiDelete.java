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
package com.helger.photon.bootstrap5.pages.handler;

import java.util.function.BiFunction;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.base.id.IHasID;
import com.helger.photon.bootstrap5.buttongroup.BootstrapButtonToolbar;
import com.helger.photon.bootstrap5.form.BootstrapForm;
import com.helger.photon.bootstrap5.pages.BootstrapWebPageUIHandler;
import com.helger.photon.uicore.page.IWebPageExecutionContext;
import com.helger.photon.uicore.page.handler.AbstractWebPageActionHandlerMultiDelete;

public abstract class AbstractBootstrapWebPageActionHandlerMultiDelete <DATATYPE extends IHasID <String>, WPECTYPE extends IWebPageExecutionContext>
                                                                       extends
                                                                       AbstractWebPageActionHandlerMultiDelete <DATATYPE, WPECTYPE, BootstrapForm, BootstrapButtonToolbar>
{
  public AbstractBootstrapWebPageActionHandlerMultiDelete (@NonNull @Nonempty final String sFieldName,
                                                           @NonNull final BiFunction <WPECTYPE, String, DATATYPE> aResolver)
  {
    super (BootstrapWebPageUIHandler.INSTANCE, sFieldName, aResolver);
  }
}
