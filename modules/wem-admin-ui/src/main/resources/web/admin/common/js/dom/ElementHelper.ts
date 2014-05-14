module api.dom {

    export class ElementHelper {

        private el: HTMLElement;

        static fromName(name: string): ElementHelper {
            api.util.assert(!api.util.isStringEmpty(name), 'Tag name shouldn\'t be empty');
            return new ElementHelper(document.createElement(name));
        }

        constructor(element: HTMLElement) {
            this.el = element;
        }

        getHTMLElement(): HTMLElement {
            return this.el;
        }

        insertBefore(newEl: Element, existingEl: Element) {
            api.util.assertNotNull(newEl, 'New element shouldn\'t be null');
            api.util.assertNotNull(existingEl, 'Existing element shouldn\'t be null');
            this.el.insertBefore(newEl.getHTMLElement(), existingEl ? existingEl.getHTMLElement() : null);
        }

        insertBeforeEl(existingEl: Element) {
            existingEl.getHTMLElement().parentNode.insertBefore(this.el, existingEl.getHTMLElement());
        }

        insertAfterEl(existingEl: Element) {
            api.util.assertNotNull(existingEl, 'Existing element shouldn\'t be null');
            existingEl.getHTMLElement().parentNode.insertBefore(this.el, existingEl.getHTMLElement().nextSibling);
        }

        /*
         * @returns {api.dom.ElementHelper} ElementHelper for previous node of this element.
         */
        getPrevious(): ElementHelper {
            var previous = this.el.previousSibling;
            while (previous && previous.nodeType != Node.ELEMENT_NODE) {
                previous = previous.previousSibling;
            }
            return previous ? new ElementHelper(<HTMLElement> previous) : null;
        }

        setDisabled(value: boolean): ElementHelper {
            this.el.disabled = value;
            return this;
        }

        isDisabled(): boolean {
            return this.el.disabled;
        }

        getId(): string {
            return this.el.id;
        }

        setId(value: string): ElementHelper {
            this.el.id = value;
            return this;
        }

        setInnerHtml(value: string): ElementHelper {
            jQuery(this.el).html(value);
            return this;
        }

        getInnerHtml(): string {
            return this.el.innerHTML;
        }

        setAttribute(name: string, value: string): ElementHelper {
            this.el.setAttribute(name, value);
            return this;
        }

        getAttribute(name: string): string {
            return this.el.getAttribute(name);
        }

        hasAttribute(name: string): boolean {
            return this.el.hasAttribute(name);
        }

        removeAttribute(name: string): ElementHelper {
            this.el.removeAttribute(name);
            return this;
        }

        setData(name: string, value: string): ElementHelper {
            api.util.assert(!api.util.isStringEmpty(name), 'Name shouldn\'t be empty');
            api.util.assert(!api.util.isStringEmpty(value), 'Value shouldn\'t be empty');
            this.el.setAttribute('data-' + name, value);
            jQuery(this.el).data(name, value);
            return this;
        }

        getData(name: string): string {
            return jQuery(this.el).data(name);
        }

        getValue(): string {
            return this.el['value'];
        }

        setValue(value: string): ElementHelper {
            this.el['value'] = value;
            return this;
        }

        addClass(clsName: string): ElementHelper {
            api.util.assert(!api.util.isStringEmpty(clsName), 'Class name shouldn\'t be empty');
            // spaces are not allowed
            var classList: string[] = clsName.split(" ");
            classList.forEach((classItem: string) => {
                if (!this.hasClass(classItem)) {
                    this.el.classList.add(classItem);
                }
            });
            return this;
        }

        setClass(value: string): ElementHelper {
            this.el.className = value;
            return this;
        }

        hasClass(clsName: string): boolean {
            api.util.assert(!api.util.isStringEmpty(clsName), 'Class name shouldn\'t be empty');
            // spaces are not allowed
            var classList: string[] = clsName.split(" ");
            for (var i = 0; i < classList.length; i++) {
                var classItem = classList[i];
                if (!this.el.classList.contains(classItem)) {
                    return false;
                }
            }
            return true;
        }

        removeClass(clsName: string): ElementHelper {
            api.util.assert(!api.util.isStringEmpty(clsName), 'Class name shouldn\'t be empty');
            // spaces are not allowed
            var classList: string[] = clsName.split(" ");
            classList.forEach((classItem: string) => {
                this.el.classList.remove(classItem);
            });
            return this;
        }

        addEventListener(eventName: string, f: (event: Event) => any): ElementHelper {
            this.el.addEventListener(eventName, f);
            return this;
        }

        removeEventListener(eventName: string, f: (event: Event) => any): ElementHelper {
            this.el.removeEventListener(eventName, f);
            return this;
        }

        appendChild(child: Node): ElementHelper {
            this.el.appendChild(child);
            return this;
        }

        appendChildren(children: Node[]): ElementHelper {
            children.forEach((child: Node) => {
                this.el.appendChild(child);
            });
            return this;
        }

        getDisplay(): string {
            return this.el.style.display;
        }

        setDisplay(value: string): ElementHelper {
            this.el.style.display = value;
            return this;
        }

        getVisibility(): string {
            return this.el.style.visibility;
        }

        setVisibility(value: string): ElementHelper {
            this.el.style.visibility = value;
            return this;
        }

        getPosition(): string {
            return this.getComputedProperty('position');
        }

        setPosition(value: string): ElementHelper {
            this.el.style.position = value;
            return this;
        }

        setWidth(value: string): ElementHelper {
            this.el.style.width = value;
            return this;
        }

        setWidthPx(value: number): ElementHelper {
            this.setWidth(value + "px");
            return this;
        }

        getWidth(): number {
            return $(this.el).innerWidth();
        }

        getWidthWithoutPadding(): number {
            return $(this.el).width();
        }

        getWidthWithBorder(): number {
            return $(this.el).outerWidth();
        }

        getWidthWithMargin(): number {
            return $(this.el).outerWidth(true);
        }

        getMinWidth(): number {
            return parseFloat(this.getComputedProperty('min-width')) || 0;
        }

        setHeight(value: string): ElementHelper {
            this.el.style.height = value;
            return this;
        }

        setHeightPx(value: number): ElementHelper {
            this.setHeight(value + "px");
            return this;
        }

        getHeight(): number {
            return $(this.el).innerHeight();
        }

        getHeightWithoutPadding(): number {
            return $(this.el).height();
        }

        getHeightWithBorder(): number {
            return $(this.el).outerHeight();
        }

        getHeightWithMargin(): number {
            return $(this.el).outerHeight(true);
        }

        setTop(value: string): ElementHelper {
            this.el.style.top = value;
            return this;
        }

        setTopPx(value: number): ElementHelper {
            return this.setTop(value + "px");
        }

        setBottom(value: string): ElementHelper {
            this.el.style.bottom = value;
            return this;
        }

        setBottomPx(value: number): ElementHelper {
            return this.setBottom(value + "px");
        }

        setLeftPx(value: number): ElementHelper {
            return this.setLeft(value + "px");
        }

        setLeft(value: string): ElementHelper {
            this.el.style.left = value;
            return this;
        }

        setRight(value: string): ElementHelper {
            this.el.style.right = value;
            return this;
        }

        getMarginLeft(): number {
            return parseFloat(this.getComputedProperty('margin-left')) || 0;
        }

        setMarginLeft(value: string): ElementHelper {
            this.el.style.marginLeft = value;
            return this;
        }

        getMarginRight(): number {
            return parseFloat(this.getComputedProperty('margin-right'));
        }

        setMarginRight(value: string): ElementHelper {
            this.el.style.marginRight = value;
            return this;
        }

        getMarginTop(): number {
            return parseFloat(this.getComputedProperty('margin-top'));
        }

        setMarginTop(value: string): ElementHelper {
            this.el.style.marginTop = value;
            return this;
        }

        getMarginBottom(): number {
            return parseFloat(this.getComputedProperty('margin-bottom'));
        }

        setMarginBottom(value: string): ElementHelper {
            this.el.style.marginBottom = value;
            return this;
        }

        getPaddingLeft(): number {
            return parseFloat(this.getComputedProperty('padding-left'));
        }

        getBorderTopWidth(): number {
            return parseFloat(this.getComputedProperty('border-top-width'));
        }

        getBorderBottomWidth(): number {
            return parseFloat(this.getComputedProperty('border-bottom-width'));
        }

        setZindex(value: number): ElementHelper {
            this.el.style.zIndex = value.toString();
            return this;
        }

        setBackgroundImage(value: string): ElementHelper {
            this.el.style.backgroundImage = value;
            return this;
        }

        remove() {
            var parent = this.el.parentElement;
            if (parent) {
                parent.removeChild(this.el);
            }
        }

        contains(element: HTMLElement): boolean {
            return this.el.contains ? this.el.contains(element) : !!(this.el.compareDocumentPosition(element) & 16);
        }

        /**
         * Calculate offset relative to document
         * @returns {{left: number, top: number}}
         */
        getOffset(): { top:number; left:number;
        } {
            return $(this.el).offset();
        }

        /**
         * Goes up the hierarchy and returns first non-statically positioned parent
         * @returns {HTMLElement}
         */
        getOffsetParent(): HTMLElement {
            return $(this.el).offsetParent()[0];
        }

        /**
         * Calculates offset relative to first positioned parent ( element with position: relative, absolute or fixed )
         * @returns {{top: number, left: number}}
         */
        getOffsetToParent(): { top:number; left:number;
        } {
            return $(this.el).position();
        }

        getOffsetTop(): number {
            return this.getOffset().top;
        }

        getOffsetTopRelativeToParent(): number {
            return this.el.offsetTop;
        }

        getOffsetLeft(): number {
            return this.getOffset().left;
        }

        getOffsetLeftRelativeToParent(): number {
            return this.el.offsetLeft;
        }

        getComputedProperty(name: string, pseudoElement: string = null): string {
            return window.getComputedStyle(this.el, pseudoElement).getPropertyValue(name);
        }

        focuse() {
            this.el.focus();
        }

        blur() {
            this.el.blur();
        }

    }
}