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
package com.helger.photon.bootstrap5.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.helger.html.hc.html.grouping.HCDiv;

/**
 * Test class for class {@link BootstrapGridSpec}.
 *
 * @author Philip Helger
 */
public final class BootstrapGridSpecTest
{
  @Test
  public void testBuilderEmpty ()
  {
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ().build ();
    assertNotNull (aSpec);
    assertNull (aSpec.getXS ());
    assertNull (aSpec.getSM ());
    assertNull (aSpec.getMD ());
    assertNull (aSpec.getLG ());
    assertNull (aSpec.getXL ());
    assertNull (aSpec.getXXL ());
  }

  @Test
  public void testBuilderEnums ()
  {
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ()
                                                     .xs (EBootstrapGridXS.XS_12)
                                                     .sm (EBootstrapGridSM.SM_6)
                                                     .md (EBootstrapGridMD.MD_4)
                                                     .lg (EBootstrapGridLG.LG_3)
                                                     .xl (EBootstrapGridXL.XL_2)
                                                     .xxl (EBootstrapGridXXL.XXL_1)
                                                     .build ();
    assertEquals (EBootstrapGridXS.XS_12, aSpec.getXS ());
    assertEquals (EBootstrapGridSM.SM_6, aSpec.getSM ());
    assertEquals (EBootstrapGridMD.MD_4, aSpec.getMD ());
    assertEquals (EBootstrapGridLG.LG_3, aSpec.getLG ());
    assertEquals (EBootstrapGridXL.XL_2, aSpec.getXL ());
    assertEquals (EBootstrapGridXXL.XXL_1, aSpec.getXXL ());

    final HCDiv aDiv = aSpec.applyTo (new HCDiv ());
    assertEquals ("col-12 col-sm-6 col-md-4 col-lg-3 col-xl-2 col-xxl-1", aDiv.getAllClassesAsString ());
  }

  @Test
  public void testBuilderParts ()
  {
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ().xs (12).sm (6).md (4).lg (3).xl (2).xxl (1).build ();
    assertEquals (EBootstrapGridXS.XS_12, aSpec.getXS ());
    assertEquals (EBootstrapGridSM.SM_6, aSpec.getSM ());
    assertEquals (EBootstrapGridMD.MD_4, aSpec.getMD ());
    assertEquals (EBootstrapGridLG.LG_3, aSpec.getLG ());
    assertEquals (EBootstrapGridXL.XL_2, aSpec.getXL ());
    assertEquals (EBootstrapGridXXL.XXL_1, aSpec.getXXL ());

    // Unknown parts lead to null
    assertNull (BootstrapGridSpec.builder ().xs (IBootstrapGridElement.PARTS_NONE).build ().getXS ());
    assertEquals (EBootstrapGridXS.AUTO,
                  BootstrapGridSpec.builder ().xs (IBootstrapGridElement.PARTS_AUTO).build ().getXS ());
    assertEquals (EBootstrapGridXS.EVENLY,
                  BootstrapGridSpec.builder ().xs (IBootstrapGridElement.PARTS_EVENLY).build ().getXS ());
  }

  @Test
  public void testBuilderAll ()
  {
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ().all (6).build ();
    // Only XS is set - the larger breakpoints inherit from it
    assertEquals (EBootstrapGridXS.XS_6, aSpec.getXS ());
    assertNull (aSpec.getSM ());
    assertNull (aSpec.getMD ());
    assertNull (aSpec.getLG ());
    assertNull (aSpec.getXL ());
    assertNull (aSpec.getXXL ());

    // Only the smallest one is emitted
    final HCDiv aDiv = aSpec.applyTo (new HCDiv ());
    assertEquals ("col-6", aDiv.getAllClassesAsString ());
  }

  @Test
  public void testGetInverse ()
  {
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ().xs (12).sm (6).md (4).lg (3).xl (2).xxl (1).build ();
    final BootstrapGridSpec aInverse = aSpec.getInverse ();
    // 12 has no counterpart and stays 12
    assertEquals (EBootstrapGridXS.XS_12, aInverse.getXS ());
    assertEquals (EBootstrapGridSM.SM_6, aInverse.getSM ());
    assertEquals (EBootstrapGridMD.MD_8, aInverse.getMD ());
    assertEquals (EBootstrapGridLG.LG_9, aInverse.getLG ());
    assertEquals (EBootstrapGridXL.XL_10, aInverse.getXL ());
    assertEquals (EBootstrapGridXXL.XXL_11, aInverse.getXXL ());

    final HCDiv aDiv = aInverse.applyTo (new HCDiv ());
    assertEquals ("col-12 col-sm-6 col-md-8 col-lg-9 col-xl-10 col-xxl-11", aDiv.getAllClassesAsString ());

    // The inverse of the inverse is the original one again
    final BootstrapGridSpec aInverse2 = aInverse.getInverse ();
    assertEquals (aSpec.getXS (), aInverse2.getXS ());
    assertEquals (aSpec.getSM (), aInverse2.getSM ());
    assertEquals (aSpec.getMD (), aInverse2.getMD ());
    assertEquals (aSpec.getLG (), aInverse2.getLG ());
    assertEquals (aSpec.getXL (), aInverse2.getXL ());
    assertEquals (aSpec.getXXL (), aInverse2.getXXL ());
  }

  @Test
  public void testGetInverseUnset ()
  {
    // XS and SM are not set at all, so they count as "0 parts" and the inverse is the maximum;
    // LG, XL and XXL inherit from MD, so their inverse inherits as well
    final BootstrapGridSpec aInverse = BootstrapGridSpec.builder ().md (5).build ().getInverse ();
    assertEquals (EBootstrapGridXS.XS_12, aInverse.getXS ());
    assertNull (aInverse.getSM ());
    assertEquals (EBootstrapGridMD.MD_7, aInverse.getMD ());
    assertNull (aInverse.getLG ());
    assertNull (aInverse.getXL ());
    assertNull (aInverse.getXXL ());

    final HCDiv aDiv = aInverse.applyTo (new HCDiv ());
    assertEquals ("col-12 col-md-7", aDiv.getAllClassesAsString ());

    // Nothing set at all means "0 parts" everywhere
    final BootstrapGridSpec aNone = BootstrapGridSpec.NONE.getInverse ();
    assertEquals (EBootstrapGridXS.XS_12, aNone.getXS ());
    assertNull (aNone.getSM ());
    assertNull (aNone.getMD ());
    assertNull (aNone.getLG ());
    assertNull (aNone.getXL ());
    assertNull (aNone.getXXL ());

    // 0 parts is the same as "not set"
    assertEquals (EBootstrapGridXS.XS_12, BootstrapGridSpec.builder ().xs (0).build ().getInverse ().getXS ());
  }

  @Test
  public void testGetInverseSpecialValues ()
  {
    // "auto" and "evenly" have no numeric counterpart and are kept as-is
    final BootstrapGridSpec aAuto = BootstrapGridSpec.builder ().xs (EBootstrapGridXS.AUTO).build ().getInverse ();
    assertEquals (EBootstrapGridXS.AUTO, aAuto.getXS ());
    assertNull (aAuto.getSM ());

    final BootstrapGridSpec aEvenly = BootstrapGridSpec.EVENLY.getInverse ();
    assertEquals (EBootstrapGridXS.EVENLY, aEvenly.getXS ());
    assertNull (aEvenly.getSM ());
  }

  @Test
  public void testBuilderCopy ()
  {
    final BootstrapGridSpec aSrc = BootstrapGridSpec.builder ().xs (12).sm (6).md (4).lg (3).xl (2).xxl (1).build ();
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder (aSrc).md (5).build ();
    assertEquals (aSrc.getXS (), aSpec.getXS ());
    assertEquals (aSrc.getSM (), aSpec.getSM ());
    assertEquals (EBootstrapGridMD.MD_5, aSpec.getMD ());
    assertEquals (aSrc.getLG (), aSpec.getLG ());
    assertEquals (aSrc.getXL (), aSpec.getXL ());
    assertEquals (aSrc.getXXL (), aSpec.getXXL ());

    // null source is allowed
    assertNull (BootstrapGridSpec.builder ((BootstrapGridSpec) null).build ().getXS ());
  }
}
