(function () {
    const { __ } = wp.i18n;
    const { InspectorControls } = wp.blockEditor;
    const { PanelBody, ToggleControl } = wp.components;
    const { addFilter } = wp.hooks;

    // List of blocks to extend
    const blocksToExtend = ['core/columns', 'core/group', 'core/media-text'];

    // Add a "reverseMobile" attribute to the specified blocks
    addFilter(
        'blocks.registerBlockType',
        'mlrg-columns-reverse/extend-block',
        (settings, name) => {
            if (!blocksToExtend.includes(name)) {
                return settings;
            }

            settings.attributes = {
                ...settings.attributes,
                reverseMobile: {
                    type: 'boolean',
                    default: false,
                },
            };

            return settings;
        }
    );

    // Add an interface to enable/disable the option in the inspector
    addFilter(
        'editor.BlockEdit',
        'mlrg-columns-reverse/add-control',
        (BlockEdit) => (props) => {
            if (!blocksToExtend.includes(props.name)) {
                return wp.element.createElement(BlockEdit, props);
            }

            const { attributes, setAttributes } = props;
            const { reverseMobile, layout } = attributes;

            // Check if the block is not a group with a flex layout (row / stack)
            if (props.name === 'core/group' && (!layout || layout.type !== 'flex')) {
                return wp.element.createElement(BlockEdit, props);
            }

            return wp.element.createElement(
                wp.element.Fragment,
                null,
                wp.element.createElement(BlockEdit, props),
                wp.element.createElement(
                    InspectorControls,
                    null,
                    wp.element.createElement(
                        PanelBody,
                        { title: __('Responsive', 'block-editor-columns-reverse') },
                        wp.element.createElement(ToggleControl, {
                            label: __('Reverse on mobile', 'block-editor-columns-reverse'),
                            checked: reverseMobile,
                            onChange: (value) => setAttributes({ reverseMobile: value }),
                        }),
                        reverseMobile &&
                        wp.element.createElement(
                            'p',
                            { style: { fontSize: '12px', color: '#757575', marginTop: '8px' } },
                            props.name === 'core/media-text'
                                ? __('Applies to screens with width ≤ 600px.', 'block-editor-columns-reverse')
                                : __('Applies to screens with width ≤ 781px.', 'block-editor-columns-reverse')
                        )
                    )
                )
            );
        }
    );

    // Add the class to the block for front-end styling and editor styling
    addFilter(
        'blocks.getSaveContent.extraProps',
        'mlrg-columns-reverse/add-class',
        (extraProps, blockType, attributes) => {
            if (blocksToExtend.includes(blockType.name) && attributes.reverseMobile) {
                extraProps.className = (extraProps.className || '') + ' mlrg-reverse-mobile';
            }

            return extraProps;
        }
    );

    // Add the class to the block in the editor
    addFilter(
        'editor.BlockListBlock',
        'mlrg-columns-reverse/add-editor-class',
        (BlockListBlock) => (props) => {
            const { attributes } = props;

            if (blocksToExtend.includes(props.name) && attributes.reverseMobile) {
                return wp.element.createElement(BlockListBlock, {
                    ...props,
                    className: (props.className || '') + ' mlrg-reverse-mobile',
                });
            }

            return wp.element.createElement(BlockListBlock, props);
        }
    );
})();
