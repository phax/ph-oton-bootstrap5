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
    final BootstrapGridSpec aSpec = BootstrapGridSpec.builder ()
                                                     .xs (12)
                                                     .sm (6)
                                                     .md (4)
                                                     .lg (3)
                                                     .xl (2)
                                                     .xxl (1)
                                                     .build ();
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
    assertEquals (EBootstrapGridXS.XS_6, aSpec.getXS ());
    assertEquals (EBootstrapGridSM.SM_6, aSpec.getSM ());
    assertEquals (EBootstrapGridMD.MD_6, aSpec.getMD ());
    assertEquals (EBootstrapGridLG.LG_6, aSpec.getLG ());
    assertEquals (EBootstrapGridXL.XL_6, aSpec.getXL ());
    assertEquals (EBootstrapGridXXL.XXL_6, aSpec.getXXL ());

    // All have the same part count, so only the smallest one is emitted
    final HCDiv aDiv = aSpec.applyTo (new HCDiv ());
    assertEquals ("col-6", aDiv.getAllClassesAsString ());
  }

  @Test
  public void testBuilderCopy ()
  {
    final BootstrapGridSpec aSrc = BootstrapGridSpec.create (12, 6, 4, 3, 2, 1);
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
