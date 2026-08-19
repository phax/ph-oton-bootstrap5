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
package com.helger.photon.bootstrap5.uictrls.datetimepicker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsHashSet;
import com.helger.collection.commons.ICommonsSet;

/**
 * Test class for class {@link ETempusDominusFormatToken}.
 *
 * @author Philip Helger
 */
public final class ETempusDominusFormatTokenTest
{
  @Test
  public void testAllTokensAreValid ()
  {
    final ICommonsSet <String> aJavaTokens = new CommonsHashSet <> ();
    for (final ETempusDominusFormatToken e : ETempusDominusFormatToken.values ())
    {
      assertTrue (e.name (), StringHelper.isNotEmpty (e.getJavaToken ()));
      assertTrue (e.name (), StringHelper.isNotEmpty (e.getJSToken ()));
      // Each Java token may only be mapped once
      assertTrue (e.name () + " has a duplicate Java token '" + e.getJavaToken () + "'",
                  aJavaTokens.add (e.getJavaToken ()));
    }
  }

  @Test
  public void testEachJavaTokenIsConvertible ()
  {
    for (final ETempusDominusFormatToken e : ETempusDominusFormatToken.values ())
      assertEquals (e.name (),
                    e.getJSToken (),
                    Bootstrap5DateTimePickerFormatBuilder.fromJavaPattern (e.getJavaToken ())
                                                         .getJSCalendarFormatString ());
  }
}
