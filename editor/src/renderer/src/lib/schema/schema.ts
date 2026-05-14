import schema from "@renderer/assets/components.schema.json";
import type { EntityComponent } from "@renderer/store/features/entitiesSlice";

type ValueOf<T> = T[keyof T];

export function schemaCompToFields(component: ValueOf<typeof schema>): EntityComponent {
    const comp: EntityComponent = {
        class: component.class
    }

    const fields = recursiveFields(component.fields)

    for(const field in fields) {
        comp[field] = fields[field]
    }

    return comp
}

const DEFAULTS = {
    "string": "",
    "float": 0,
    "int": 0,
    "boolean": false,
}

function recursiveFields(fields: ValueOf<typeof schema>["fields"]) {
    const obj: {[key: string]: any} = {}

    for(const key in fields) {
        const value = fields[key]


        if(typeof value === "object") {
            console.log("OBJ", value)

            const subfields = value

            if(subfields["type"]) {
            
                const type = subfields["type"];
                if(type === "enum") {

                    obj[key] = subfields["values"][0]
                    console.log("ENUM", obj)
                    continue;
                }

            }


            console.log("SUB", key, subfields["fields"])
            const subProperties = recursiveFields(subfields["fields"])
            obj[key] = {}

            for(const subfield in subProperties) {
                console.log(key, subfield, subProperties[subfield])
                obj[key][subfield] = subProperties[subfield]
            }

            continue;
        }

        obj[key] = DEFAULTS[value]
    }

    console.log("FINISHED", obj)

    return obj
}
