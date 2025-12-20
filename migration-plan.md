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

#### 2.1 Bootstrap 5 Assets

- Download Bootstrap 5.3.x (latest stable) CSS and JS files
- Place in `src/main/resources/external/bootstrap/5.3.x/`
- Include both regular and minified versions

#### 2.2 Update Constants

- **CBootstrap.java**: Update version constant to Bootstrap 5.3.x
- **CBootstrapCSS.java**: Extract all Bootstrap 5 CSS classes
  - Remove deprecated classes (jumbotron, media, etc.)
  - Add new classes (offcanvas, accordion-flush, etc.)
  - Update utility classes (spacing changed from `-*` to `-*`)
  - Note: Bootstrap 5 removed many classes and changed naming

#### 2.3 Key Bootstrap 5 Changes to Implement

**Removed Components:**
- Jumbotron → Replace with utilities
- Media object → Use flex utilities
- Card deck/columns → Use grid

**New Components:**
- Offcanvas (new sliding sidebar)
- Floating labels for forms
- Accordion improvements
- Updated modal backdrop

**Modified Components:**
- **Forms**: Major restructuring
  - `.form-control` updates
  - New floating labels
  - Updated validation styles
- **Buttons**: Simplified sizes
- **Grid**: New `xxl` breakpoint
- **Dropdowns**: Improved positioning
- **Navbar**: Simplified classes

#### 2.4 Update Path Providers

- **EBootstrapCSSPathProvider.java**: Update paths to 5.3.x
- **EBootstrapJSPathProvider.java**: Update paths to 5.3.x
- Note: Bootstrap 5 dropped jQuery dependency (important!)

#### 2.5 Create/Update Component Classes

For each component package, update:

**alert/** - Minimal changes
- Update CSS class references

**badge/** - Minor updates
- Removed `badge-pill`, now `rounded-pill`

**breadcrumb/** - Minimal changes

**button/** - Updates needed
- Remove `.btn-block` (use grid/flex instead)
- Update size classes

**buttongroup/** - Minimal changes

**card/** - Significant changes
- Remove `card-deck` and `card-columns` classes
- Update to use grid system instead

**dropdown/** - Updates needed
- New `data-bs-toggle` (was `data-toggle`)
- New `data-bs-` prefix for all data attributes

**form/** - Major restructuring needed
- New form control styling
- Floating labels support
- Updated validation classes
- Switch from custom controls to standard

**grid/** - Updates needed
- Add `xxl` breakpoint support (≥1400px)
- Keep existing sm, md, lg, xl

**inputgroup/** - Updates needed
- Simplified markup

**layout/** - Updates needed

**listgroup/** - Minor updates

**modal/** - Updates needed
- New `data-bs-` attributes
- Update JavaScript method calls

**nav/navbar/** - Updates needed
- Simplified navbar classes
- Remove unnecessary wrappers

**table/** - Minimal changes
- New accent colors

**tooltip/popover** - Updates needed
- New Popper.js v2 integration
- Bootstrap 5 uses Popper v2

**NEW: offcanvas/** - Add new component
- Create `BootstrapOffcanvas.java`
- Handle positioning (start, end, top, bottom)

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
