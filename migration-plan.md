# Migration Plan: Bootstrap 4 to Bootstrap 5 Java Wrapper

Based on analysis of the `ph-oton-bootstrap4` project, this document provides a comprehensive plan to create the same functionality for Bootstrap 5.

## Project Overview

The Bootstrap 4 wrapper consists of 5 modules:

1. **ph-oton-bootstrap4** - Core Java wrapper for Bootstrap 4 controls
2. **ph-oton-bootstrap4-uictrls** - Special UI controls
3. **ph-oton-bootstrap4-pages** - Predefined UI pages
4. **ph-oton-bootstrap4-stub** - Servlet stub for web applications
5. **ph-oton-bootstrap4-demo** - Demo web application

---

## Step-by-Step Migration Plan

### Phase 1: Project Setup & Infrastructure

#### 1.1 Create Maven Multi-Module Structure

- Create parent POM (`pom.xml`) similar to bootstrap4 parent
- Update artifact IDs: `ph-oton-bootstrap5-parent-pom`
- Set up 5 child modules:
  - `ph-oton-bootstrap5`
  - `ph-oton-bootstrap5-uictrls`
  - `ph-oton-bootstrap5-pages`
  - `ph-oton-bootstrap5-stub`
  - `ph-oton-bootstrap5-demo`

#### 1.2 Update Project Metadata

- Update SCM URLs to point to `ph-oton-bootstrap5` repository
- Update Maven coordinates (groupId stays: `com.helger.photon`)
- Set initial version (e.g., `1.0.0-SNAPSHOT`)
- Update README.md with Bootstrap 5 information

#### 1.3 Setup Build Configuration

- Configure same build plugins (jscompress, csscompress, bundle plugin)
- Set Java 17+ requirement
- Configure JakartaEE 10 dependencies

---

### Phase 2: Core Module (ph-oton-bootstrap5)

#### 2.1 Bootstrap 5 Assets ✅ COMPLETED

- ✅ Downloaded Bootstrap 5.3.8 (latest stable) CSS and JS files
- ✅ Placed in `src/main/resources/external/bootstrap/5.3.8/`
- ✅ Included both regular versions (minified versions created via ph-csscompress-maven-plugin)
- ✅ Created `MainExtractBootstrap5CSSClasses.java` (based on `MainExtractBootstrap4CSSClasses.java`)
  - ✅ Updated the class to read from Bootstrap 5 CSS path
  - ✅ Changed `EBootstrapCSSPathProvider.BOOTSTRAP` reference to point to Bootstrap 5 CSS
  - ✅ Ran the extraction tool to generate CSS class constants
  - ✅ Generated 2,024 CSS class constants to /tmp/bootstrap5-css-classes.txt

#### 2.2 Update Constants ✅ COMPLETED

- ✅ **CBootstrap.java**: Updated version constant to Bootstrap 5.3.8 (BOOTSTRAP_VERSION_538)
- ✅ **CBootstrapCSS.java**: Extracted all 2,024 Bootstrap 5 CSS classes (2,060 lines)
  - ✅ Removed deprecated classes (jumbotron-*, media-*, etc.)
  - ✅ Added new classes (offcanvas-*, accordion-flush, etc.)
  - ✅ Updated utility classes (fw-bold, fst-italic, font-monospace, etc.)
  - ✅ Added XXL breakpoint classes (col-xxl-*, etc.)

#### 2.3 Key Bootstrap 5 Changes to Implement

**Bootstrap 4 Component Inventory (121 classes across 17 packages):**
- **alert/** (9 classes): AbstractBootstrapAlert, BootstrapBox, BootstrapDangerBox, BootstrapErrorBox, BootstrapInfoBox, BootstrapQuestionBox, BootstrapSuccessBox, BootstrapWarnBox, EBootstrapAlertType
- **badge/** (3 classes): BootstrapBadge, BootstrapBadgeLink, EBootstrapBadgeType
- **breadcrumb/** (4 classes): BootstrapBreadcrumb, BootstrapBreadcrumbItem, BootstrapBreadcrumbList, BootstrapBreadcrumbProvider
- **button/** (6 classes): BootstrapButton, BootstrapLinkButton, BootstrapResetButton, BootstrapSubmitButton, EBootstrapButtonSize, EBootstrapButtonType
- **buttongroup/** (4 classes): BootstrapButtonGroup, BootstrapButtonToolbar, EBootstrapButtonGroupSize, EBootstrapButtonGroupType
- **card/** (7 classes): BootstrapCard, BootstrapCardBody, BootstrapCardColumns, BootstrapCardDeck, BootstrapCardFooter, BootstrapCardGroup, BootstrapCardHeader
- **dropdown/** (6 classes): BootstrapDropdownDivider, BootstrapDropdownHeader, BootstrapDropdownItem, BootstrapDropdownMenu, BootstrapDropdownText, EBootstrapDropType
- **form/** (12 classes): BootstrapForm, BootstrapFormCheck, BootstrapFormCheckInline, BootstrapFormGroup, BootstrapFormGroupRendererTextOnly, BootstrapFormHelper, BootstrapInvalidFeedback, BootstrapViewForm, DefaultBootstrapFormGroupRenderer, EBootstrapFormType, IBootstrapFormGroupContainer, IBootstrapFormGroupRenderer
- **grid/** (12 classes): BootstrapCol, BootstrapGridSpec, BootstrapRow, EBootstrapColOrder, EBootstrapGridLG, EBootstrapGridMD, EBootstrapGridSM, EBootstrapGridType, EBootstrapGridXL, EBootstrapGridXS, EBootstrapRowVerticalAlign, IBootstrapGridElement
- **inputgroup/** (2 classes): BootstrapInputGroup, EBootstrapInputGroupSize
- **jumbotron/** (1 class): BootstrapJumbotron
- **listgroup/** (1 class): BootstrapListGroup
- **modal/** (4 classes): BootstrapModal, BootstrapModalCloseButton, EBootstrapModalOptionBackdrop, EBootstrapModalSize
- **nav/** (5 classes): BootstrapNav, BootstrapNavItem, BootstrapNavLink, BootstrapTabBox, EBootstrapNavType
- **navbar/** (7 classes): BootstrapNavbar, BootstrapNavbarNav, BootstrapNavbarText, BootstrapNavbarToggleable, BootstrapNavbarToggler, EBootstrapNavbarColorSchemeType, EBootstrapNavbarExpandType
- **table/** (2 classes): AbstractBootstrapTable, BootstrapTable
- **tooltip/** (5 classes): BootstrapTooltip, EBootstrapTooltipBoundary, EBootstrapTooltipFallbackPlacement, EBootstrapTooltipPosition, EBootstrapTooltipTrigger
- **utils/** (29 classes): Utility helpers for borders, spacing, display, colors, etc.

**Bootstrap 5 Migration Changes:**

**Components to Remove:**
- **jumbotron/BootstrapJumbotron** → Document migration to utility classes
- **card/BootstrapCardDeck** → Use grid system instead
- **card/BootstrapCardColumns** → Use grid system instead

**New Components to Add:**
- **offcanvas/** package (NEW)
  - BootstrapOffcanvas
  - BootstrapOffcanvasHeader
  - BootstrapOffcanvasBody
  - EBootstrapOffcanvasPosition (start, end, top, bottom)
- **form/** additions
  - BootstrapFloatingLabel (new form control variant)
  - BootstrapValidFeedback (counterpart to InvalidFeedback)

**Components Requiring Major Updates:**
- **Forms** (12 classes → ~14 classes with additions)
  - Custom form controls merged with native controls
  - Add floating label support
  - Update validation feedback styling
  - Remove custom-* CSS class patterns
- **Buttons** (6 classes)
  - Remove `.btn-block` support (deprecated)
  - Document use of `.d-grid` instead
- **Grid** (12 classes → add XXL breakpoint)
  - Add `EBootstrapGridXXL` enum (≥1400px)
  - Update all grid-related classes to support XXL
- **Navbar** (7 classes)
  - Update `EBootstrapNavbarExpandType` to include XXL
  - Simplify data attributes (data-bs-* prefix)

**Components Requiring Minor Updates:**
- **Badge** (3 classes) - Replace badge-pill with rounded-pill utility
- **Dropdown** (6 classes) - Update data attributes (data-bs-* prefix)
- **Modal** (4 classes) - Update data attributes, add new fullscreen modes
- **Tooltip** (5 classes) - Update for Popper.js v2, data-bs-* prefix
- **Alert** (9 classes) - Minimal CSS class updates
- **Breadcrumb** (4 classes) - Minimal changes
- **Button Group** (4 classes) - Minimal changes
- **Input Group** (2 classes) - Simplified markup
- **List Group** (1 class) - Minimal changes
- **Nav** (5 classes) - Minor CSS updates, add nav-underline support
- **Table** (2 classes) - Add new table variants (striped-columns, etc.)

#### 2.4 Update Path Providers ✅ COMPLETED

- ✅ **EBootstrapCSSPathProvider.java**: Updated to Bootstrap 5.3.8
- ✅ **EBootstrapJSPathProvider.java**: Created with Bootstrap 5.3.8 paths
  - ✅ BOOTSTRAP: external/bootstrap/5.3.8/bootstrap.js (requires Popper.js separately)
  - ✅ BOOTSTRAP_BUNDLE: external/bootstrap/5.3.8/bootstrap.bundle.js (includes Popper.js - recommended)
  - ✅ BOOTSTRAP_PH: ph-oton/bootstrap5-ph.js (custom extensions)
- ✅ Note: Bootstrap 5 uses Popper.js v2 (jQuery no longer required!)

#### 2.5 Migrate Component Classes (Package by Package)

**Migration Priority Order:**

**Phase 2.5.1 - Minimal Changes (Quick Wins)** ✅ COMPLETED
1. ✅ **alert/** (9 classes) - Migrated, CSS class references updated
2. ✅ **breadcrumb/** (4 classes) - Migrated, minimal CSS updates applied
3. ✅ **buttongroup/** (4 classes) - Migrated, minimal changes
4. ✅ **listgroup/** (1 class) - Migrated, minimal changes

**Phase 2.5.2 - Minor Updates** ✅ COMPLETED
5. ✅ **badge/** (3 classes) - Migrated (badge-pill → rounded-pill update needed)
6. ✅ **inputgroup/** (2 classes) - Migrated, simplified markup patterns
7. ✅ **nav/** (5 classes) - Migrated (nav-underline support to be added)
8. ✅ **table/** (2 classes) - Migrated (new table variants to be added)
9. ✅ **tooltip/** (5 classes) - Migrated (Popper.js v2 and data-bs-* updates needed)

**Phase 2.5.3 - Moderate Updates** ✅ COMPLETED
10. ✅ **dropdown/** (6 classes) - Migrated (data-toggle → data-bs-toggle updates needed)
11. ✅ **modal/** (4 classes) - Migrated (data attributes and fullscreen modes to be added)
12. ✅ **navbar/** (7 classes) - Migrated (XXL breakpoint and data attributes to be updated)
13. ✅ **card/** (7 classes) - Migrated (CardDeck & CardColumns deprecation to be documented)

**Phase 2.5.4 - Major Updates**  
14. ✅ **button/** (6 classes) - Migrated (btn-block removal and d-grid alternative to be documented)
15. ✅ **grid/** (12 classes) - Migrated (EBootstrapGridXXL addition pending)
16. ✅ **form/** (12 classes) - Migrated (floating labels and validation updates pending)

**Phase 2.5.5 - Deprecations & New Components**
17. ✅ **jumbotron/** (1 class) - Migrated (@Deprecated marker and migration docs pending)
18. **offcanvas/** (NEW package, ~4 classes) - TO BE CREATED

**Phase 2.5.6 - Utilities & Base** ✅ COMPLETED
19. ✅ **utils/** (29 classes) - Migrated, updated EBootstrapFontType for BS5 utility class names (fw-*, fst-*)
20. ✅ **base/** (2 classes) - Migrated (AbstractBootstrapDiv, AbstractBootstrapObject)
21. **config/** - TO BE UPDATED

**Migration Summary:**
- **Migrated Packages:** 18/18 packages (including base and utils)
- **Total Classes Migrated:** 121 classes
- **Compilation Status:** ✅ BUILD SUCCESS
- **Known CSS Updates Needed:**
  - EBootstrapFontType: ✅ Updated (FONT_WEIGHT_BOLD → FW_BOLD, TEXT_MONOSPACE → FONT_MONOSPACE, etc.)
  - Badge pill classes: badge-pill → rounded-pill (pending)
  - Button block: btn-block → d-grid + gap utilities (pending)
  - Data attributes: data-* → data-bs-* throughout (pending)
  - Grid XXL breakpoint: Add EBootstrapGridXXL enum (pending)

#### 2.6 Update Third-Party Module Provider

- Update `config/ThirdPartyModuleProvider_ph_oton_bootstrap5.java`

---

### Phase 3: UI Controls Module (ph-oton-bootstrap5-uictrls)

#### 3.1 External Dependencies

- Update DataTables integration (if applicable)
- Update DateTimePicker (may need Bootstrap 5 compatible version)
- Update any other third-party controls

#### 3.2 Custom Controls

- Migrate `BootstrapCardCollapsible`
- Update any custom extensions
- Test compatibility with new CSS

---

### Phase 4: Pages Module (ph-oton-bootstrap5-pages)

#### 4.1 Update Base Pages

- `AbstractBootstrapWebPageForm`
- `BootstrapPagesMenuConfigurator`

#### 4.2 Update System Info Pages

- All `BasePageSysInfo*` classes
- Update form layouts for Bootstrap 5

#### 4.3 Update Application Info Pages

- All `BasePageAppInfo*` classes
- Update table and card layouts

#### 4.4 Update Utility Pages

- `BasePageUtilsPortChecker`
- Others as needed

---

### Phase 5: Stub Module (ph-oton-bootstrap5-stub)

#### 5.1 Servlet Configuration

- Update `PhotonStubServletInitializer`
- Update `PhotonStubServletContextListener`
- Update `PhotonStubServletContainerInitializer`

#### 5.2 Default Configurations

- Update CSS/JS path references
- Update initialization code

---

### Phase 6: Demo Module (ph-oton-bootstrap5-demo)

#### 6.1 Create Demo Application

- Port demo pages from bootstrap4-demo
- Showcase all Bootstrap 5 components
- Add examples of new components (offcanvas, etc.)

#### 6.2 Configuration

- Update `web.xml` for JakartaEE 10
- Update Jetty configuration (12.x)
- Test with Tomcat 10.1+

---

### Phase 7: Testing & Documentation

#### 7.1 Testing

- Unit tests for all components
- Integration tests for demo app
- Browser compatibility testing
- Responsive design testing

#### 7.2 Documentation

- Update README with Bootstrap 5 specific info
- Document breaking changes from Bootstrap 4
- Create migration guide
- Update JavaDoc

#### 7.3 Examples & Samples

- Create example code for each component
- Document new Bootstrap 5 features
- Add comparison examples (B4 vs B5)

---

## Critical Bootstrap 5 Breaking Changes to Address

### 1. jQuery Removal
Bootstrap 5 no longer requires jQuery
- Update all JavaScript interactions
- Use vanilla JavaScript or keep jQuery as optional

### 2. Data Attributes
`data-*` → `data-bs-*`
- Update all data attribute setters
- Example: `data-toggle` → `data-bs-toggle`

### 3. Form Controls
Complete restructuring
- Custom forms merged with native controls
- New floating labels
- Updated validation

### 4. Grid
New XXL breakpoint
- Add `EBootstrapGridType.XXL`
- Update `BootstrapGridSpec`

### 5. Removed Components
- Jumbotron (use utilities)
- Card deck/columns (use grid)
- Media object (use flex)

### 6. Color System
New color utilities
- Updated theme colors
- New accent colors for tables

### 7. Popper.js
Upgraded to v2
- Update tooltip/popover/dropdown positioning

### 8. Utilities
Enhanced utility API
- New utility classes
- Responsive utilities

---

## Recommended Implementation Order

| Timeline | Tasks |
|----------|-------|
| **Week 1-2** | Project setup, parent POM, core module structure |
| **Week 3-4** | Bootstrap 5 assets, constants (CBootstrap, CBootstrapCSS) |
| **Week 5-6** | Basic components (alert, badge, button, card, grid) |
| **Week 7-8** | Forms, input groups, validation |
| **Week 9-10** | Modal, dropdown, navbar, new offcanvas |
| **Week 11-12** | UI controls module, pages module |
| **Week 13-14** | Stub module, demo application |
| **Week 15-16** | Testing, documentation, refinement |

---

## Tools & Resources Needed

- Bootstrap 5.3.x official documentation
- Bootstrap 4 to 5 migration guide
- CSS class extraction tool (for `CBootstrapCSS.java`)
- Browser testing tools
- Maven 3.8+
- Java 17+

---

## Summary

This plan provides a systematic approach to migrating your Bootstrap 4 Java wrapper to Bootstrap 5, maintaining the same architectural patterns while accommodating Bootstrap 5's significant changes.
