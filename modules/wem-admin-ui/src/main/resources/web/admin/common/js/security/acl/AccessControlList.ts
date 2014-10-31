module api.security.acl {

    export class AccessControlList {

        private entries: {[key: string]: AccessControlEntry};

        constructor() {
            this.entries = {};
        }

        getEntries(): AccessControlEntry[] {
            var values = [];
            for (var key in this.entries) {
                if (this.entries.hasOwnProperty(key)) {
                    values.push(this.entries[key]);
                }
            }
            return values;
        }

        add(entry: AccessControlEntry): void {
            this.entries[entry.getPrincipalKey().toString()] = entry;
        }

        addAll(entries: AccessControlEntry[]): void {
            entries.forEach((entry) => {
                this.entries[entry.getPrincipalKey().toString()] = entry;
            });
        }

        remove(principal: PrincipalKey): void {
            delete this.entries[principal.toString()];
        }

        static fromJson(json: api.security.acl.AccessControlEntryJson[]): AccessControlList {
            var acl = new AccessControlList();
            json.forEach((entryJson: api.security.acl.AccessControlEntryJson) => {
                var entry = AccessControlEntry.fromJson(entryJson);
                acl.add(entry);
            });
            return acl;
        }
    }
}