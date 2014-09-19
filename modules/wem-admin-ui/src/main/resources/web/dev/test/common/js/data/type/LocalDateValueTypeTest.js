describe("api.data.type.LocalDateValueType", function () {

    var Value = api.data.Value;
    var ValueTypes = api.data.type.ValueTypes;

    describe("when isValid", function () {

        it("given a date as Date then true is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isValid(new Date(2000, 0, 1, 0, 0, 0))).toBe(true);
        });

        it("given a date as string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isValid("2000-01-01")).toBe(false);
        });

        it("given a letter as string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isValid("a")).toBe(false);
        });

        it("given an empty string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isValid("")).toBe(false);
        });
    });

    describe("when isConvertible", function () {

        it("given a date as string then true is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isConvertible("2000-01-01")).toBeTruthy();
        });

        it("given a partly date as string then true is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isConvertible("2000-01")).toBeFalsy();
        });

        it("given a letter as string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isConvertible("a")).toBeFalsy();
        });

        it("given an empty string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isConvertible("")).toBeFalsy();
        });

        it("given an blank string then false is returned", function () {
            expect(ValueTypes.LOCAL_DATE.isConvertible(" ")).toBeFalsy();
        });
    });

    describe("when newValue", function () {

        it("given date string '2000-01-01' then a new Value with that date is returned", function () {
            var actual = ValueTypes.LOCAL_DATE.newValue("2000-01-01");
            var expected = new Value(new Date(Date.UTC(2000, 0, 1)), ValueTypes.LOCAL_DATE);
            expect(actual).toEqual(expected);
        });

        it("given invalid date string '2000-01' then a null is returned", function () {
            expect(ValueTypes.LOCAL_DATE.newValue("2000-01")).toEqual(new Value(null, ValueTypes.LOCAL_DATE));
        });

        it("given an empty string then a null is returned", function () {
            expect(ValueTypes.LOCAL_DATE.newValue("")).toEqual(new Value(null, ValueTypes.LOCAL_DATE));
        });
    });

    describe("when toJsonValue", function () {

        it("given date 2000-01-01 as string then an equal date string is returned", function () {
            expect(ValueTypes.LOCAL_DATE.toJsonValue(ValueTypes.LOCAL_DATE.newValue("2000-01-01"))).toEqual("2000-01-01");
        });

        it("given date 2000-01-02 then an equal date string is returned", function () {
            expect(ValueTypes.LOCAL_DATE.toJsonValue(new Value(new Date(Date.UTC(2000, 0, 2)),
                ValueTypes.LOCAL_DATE))).toEqual("2000-01-02");
        });

        it("given date 2000-09-06 then an equal date string is returned", function () {
            expect(ValueTypes.LOCAL_DATE.toJsonValue(new Value(new Date(Date.UTC(2000, 8, 6)),
                ValueTypes.LOCAL_DATE))).toEqual("2000-09-06");
        });
    });

});