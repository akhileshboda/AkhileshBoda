<?php
/*
Plugin Name: Block Editor: Reverse Columns on Mobile
Description: Adds a "Reverse on Mobile" option to the Columns, Row, Stack and Media & Text blocks in Gutenberg.
Version: 1.0.7
Author: Mickaël Larguier
Author URI: https://mickaellarguier.com
License: GPL2
License URI: https://www.gnu.org/licenses/gpl-2.0.html
Text Domain: block-editor-columns-reverse
*/

if ( ! defined( 'ABSPATH' ) ) {
    exit;
}

/**
 * Enqueue necessary scripts and styles.
 */
function mlrg_enqueue_block_editor_assets() {
    wp_enqueue_script(
        'mlrg-columns-reverse-script',
        plugins_url( 'block-extend.js', __FILE__ ),
        ['wp-blocks', 'wp-i18n', 'wp-element', 'wp-components', 'wp-editor'],
        '1.0.7',
        true
    );

    wp_enqueue_style(
        'mlrg-columns-reverse-editor-style',
        plugins_url( 'style.css', __FILE__ ),
        [],
        '1.0.7'
    );
}
add_action( 'enqueue_block_editor_assets', 'mlrg_enqueue_block_editor_assets' );

/**
 * Add front-end styles to handle column reversal.
 */
function mlrg_enqueue_frontend_styles() {
    wp_enqueue_style(
        'mlrg-columns-reverse-frontend',
        plugins_url( 'style.css', __FILE__ ),
        [],
        '1.0.7'
    );
}
add_action( 'wp_enqueue_scripts', 'mlrg_enqueue_frontend_styles' );
