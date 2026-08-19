# ph-oton-bootstrap5

Type-safe Java wrappers for the Bootstrap 5 components. Everything else in this repository builds on this module. See the root `CLAUDE.md` for the repo-wide rules.

## How a component is built

Components extend `AbstractBootstrapDiv` (own base, for `<div>`-ish components) or an `AbstractHC*` class from ph-oton-html directly (e.g. `BootstrapBadge extends AbstractHCSpan`).

**CSS classes are applied in `onFinalizeNodeState (...)`, not in the constructor.** That method runs during conversion to the micro DOM, so `addClass (...)` there also picks up state that was set after construction. Consequences:

* A unit test must *render* the node to see its classes — inspecting the object is not enough.
* `super.onFinalizeNodeState (aConversionSettings, aTargetNode)` must be called first when overriding.
* Constructor-time `addClass` is only correct for classes that can never change (`BootstrapValidFeedback`, `BootstrapListGroup`).

Components that would render as an empty element override `canConvertToMicroNode (...)` to return `false` (see `AbstractBootstrapAlert`) so nothing is emitted at all.

## CSS class constants

`CBootstrapCSS` holds ~2000 `ICSSClassProvider` constants. `MainExtractBootstrap5CSSClasses` (test tree, `supplementary/tools/`) parses the shipped `bootstrap.css` and prints the constant declarations for every class selector it finds — run it and take its output when the Bootstrap version changes, rather than hand-maintaining the list. `CBootstrapCSSTest` guards that every constant is non-empty, space-free and unique.

`IHCBootstrap5Trait` (convenience factory methods for pages) is printed the same way, by `MainCreateHCBootstrap5TraitsCode`.

## Testing

`com.helger.photon.bootstrap5.mock.BootstrapTestHelper.getAsHTMLString (aNode)` renders a node without indentation, so the whole markup can be asserted with a single `assertEquals`. Nothing in this module needs a web scope.

Auto-generated element IDs (`ensureID ()`) differ per run — assert against `aNode.getID ()`, never against a literal `id10002`.

`BootstrapNoBootstrap4MarkupTest` renders one node list containing every major component and fails if any Bootstrap 3/4 leftover (`input-group-prepend`, `data-toggle=`, `float-left`, `class="close"`, …) shows up. Extend `OUTDATED_MARKUP` there when porting further components.

## Vendored Bootstrap

`src/main/resources/external/bootstrap/5.3.8/` holds the six official dist files (regular + `.min` of `bootstrap.css`, `bootstrap.js`, `bootstrap.bundle.js`), byte-identical to the upstream release. The version also appears in `CBootstrap.BOOTSTRAP_VERSION_538`, in both path provider enums and in `ThirdPartyModuleProvider_ph_oton_bootstrap5` — see the root `CLAUDE.md` for the bump checklist.
