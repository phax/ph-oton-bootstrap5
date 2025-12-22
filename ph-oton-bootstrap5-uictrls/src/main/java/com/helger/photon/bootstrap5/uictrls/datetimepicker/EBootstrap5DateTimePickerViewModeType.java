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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;

/**
 * Defines the different views of DTP.
 *
 * @author Philip Helger
 */
public enum EBootstrap5DateTimePickerViewModeType
{
  CLOCK ("clock"),
  CALENDAR ("calendar"),
  MONTHS ("months"),
  YEARS ("years"),
  DECADES ("decades");

  public static final EBootstrap5DateTimePickerViewModeType DEFAULT = CALENDAR;

  private final String m_sJSValue;

  EBootstrap5DateTimePickerViewModeType (@NonNull @Nonempty final String sJSValue)
  {
    m_sJSValue = sJSValue;
  }

  @NonNull
  @Nonempty
  public String getJSValueString ()
  {
    return m_sJSValue;
  }
}
