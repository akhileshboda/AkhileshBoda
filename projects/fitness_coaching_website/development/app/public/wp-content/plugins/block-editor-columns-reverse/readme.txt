=== Block Editor: Reverse Columns on Mobile ===
Contributors:      mickaellrg
Donate link:       https://www.paypal.com/donate/?business=W2WSLYJSSGXB6&no_recurring=0&currency_code=EUR
Tags:              gutenberg, block, columns, responsive, mobile
Requires at least: 5.3
Tested up to:      6.8
Stable tag:        1.0.7
Requires PHP:      7.0
License:           GPL-2.0-or-later
License URI:       https://www.gnu.org/licenses/gpl-2.0.html

Adds a "Reverse on Mobile" option to the Columns, Row, Stack and Media & Text blocks in Gutenberg.

== Description ==

This plugin adds a "Reverse on Mobile" option to the Columns, Row, Stack and Media & Text blocks in the WordPress Gutenberg block editor. This option allows you to reorder columns in mobile view for better presentation.

== Installation ==

1. Download the plugin.
2. Unzip the downloaded file.
3. Upload the `block-editor-columns-reverse` folder to the `/wp-content/plugins/` directory.
4. Activate the plugin through the 'Plugins' menu in WordPress.

== Frequently Asked Questions ==

= Which blocks are supported? =

The `core/columns`, `core/group` (flex layouts), and `core/media-text` blocks are supported.

= How do I enable the "Reverse on Mobile" option? =

In the block editor, select a Columns, Row, Stack or Media & Text block, then go to the block settings in the sidebar. You will see an option to "Reverse on Mobile".

= What is the breakpoint for the mobile view? =

The mobile view breakpoint is set at less than 782px, matching the one used for the `core/columns` block. When the screen width is 781px or smaller, columns will reverse their order if the "Reverse on Mobile" option is enabled.

For the `core/media-text` block, the default breakpoint is 600px or below.

== Changelog ==

= 1.0.7 =
* Support for 6.8.X.

= 1.0.6 =
* Refactor responsive styles.
* Add support for `core/media-text` breakpoint (<= 600px).

= 1.0.5 =
* Removed the 'Reverse on mobile' option in `core/group` when the layout is undefined.

= 1.0.4 =
* Fix: Resolved the issue where 'Reverse on mobile' always stacked elements on mobile, even when 'Stack on mobile' was turned off.
* Separated CSS for front-end and editor.

= 1.0.3 =
* Added support for `core/media-text` block.
* Removed the option when the group block is in the default constrained layout.

= 1.0.2 =
* Improvements to avoid potential conflicts with third-party code.

= 1.0.1 =
* Support for 6.7.X.
* Update text domain for i18n consistency.

= 1.0.0 =
* Initial release of the plugin.

== Screenshots ==

1. "Reverse on Mobile" option in the block settings.
2. Example of columns reversed in mobile view.
