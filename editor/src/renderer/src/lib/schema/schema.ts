import schema from "@renderer/assets/components.schema.json";
import type { Entity, EntityComponent } from "@renderer/store/features/entitiesSlice";

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

            const subfields = value

            if(subfields["type"]) {
            
                const type = subfields["type"];
                if(type === "enum") {

                    obj[key] = subfields["values"][0]
                    continue;
                }

            }


            const subProperties = recursiveFields(subfields["fields"])
            obj[key] = {}

            for(const subfield in subProperties) {
                obj[key][subfield] = subProperties[subfield]
            }

            continue;
        }

        obj[key] = DEFAULTS[value]
    }


    return obj
}


/**
 * WARNING! This function mutates then entity and therefore is not applicable to reduxjs state.
 * Only use as preprocess before inserting into reduxjs.
 * @param entity 
 */
export function migrateEntityToSchema(entity: Entity) {
    const { components } = entity

    const componentsToRemove: EntityComponent[] = []

    for(let i = 0; i < components.length; i++) {
        const comp = components[i]

        const schemaComp = Object.values(schema).find(e => e.class === comp.class)
        if(!schemaComp) {
            componentsToRemove.push(comp)
            continue
        }

        
        const schemaFields = recursiveFields(schemaComp.fields)
        
        const compFields = Object.keys(comp)

        recursiveDelete(comp, compFields, schemaFields)
        // for(const field of compFields) {
        //     if(field === "class") continue;
        //     if(schemaFields[field] === undefined) {
        //         // this is where we have data that doesnt exist in the schema
        //         // so we delete it
        //         delete comp[field]
        //     }
        // }

        recursiveAdd(comp, schemaFields)

        // for(const field in schemaFields) {
        //     if(comp[field] === undefined) {
        //         // this is where we dont have a field yet
        //         // so we create default values
        //         console.log("NOT FOUND", field, "in", comp, schemaFields, compFields[field])
        //     }
        // }
    }

    for(const comp of componentsToRemove) {
        const index = components.indexOf(comp)
        if(index < 0) {
            continue;
        }
        components.splice(index, 1)
    }
}

function recursiveDelete(comp: object, compFields: string[], schemaFields: object) {
    for(const field of compFields) {
        if(field === "class") continue;
        const val = schemaFields[field]
        if(val === undefined) {
            delete comp[field]
            continue
        }
        if(typeof val === "object") {
            recursiveDelete(comp, Object.keys(val), schemaFields[field])
        }
    }
}

function recursiveAdd(obj: object, fields: object) {
    for(const field in fields) {
        const val = obj[field]
        if(val === undefined) {
            // add default values
            const def = JSON.parse(JSON.stringify(fields[field]))

            obj[field] = def

            continue
        } 
        if(typeof val === "object" && typeof fields[field] === "object") {
            recursiveAdd(val, fields[field])
        }
    }
}