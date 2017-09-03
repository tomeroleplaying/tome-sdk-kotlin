
package game.engine.mechanic


import com.sun.org.apache.xpath.internal.operations.Variable
import effect.effError
import effect.effValue
import game.engine.variable.VariableId
import lulo.document.*
import lulo.value.UnexpectedType
import lulo.value.ValueParser
import java.io.Serializable


/**
 * Mechanic
 */
data class Mechanic(val mechanicId : MechanicId,
                    val label : String,
                    val description : String,
                    val summary : String,
                    val categoryId : MechanicCategoryId,
                    val requirements : List<VariableId>,
                    val variables : Set<Variable>)
                     : java.io.Serializable
{


    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<Mechanic> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::Mechanic,
                        // Mechanic Id
                        doc.at("id") ap { MechanicId.fromSchemaDoc(it) },
                        // Label
                        doc.text("label"),
                        // Description
                        doc.text("description"),
                        // Summary
                        doc.text("summary"),
                        // Category Id
                        doc.at("category_id") ap { MechanicCategoryId.fromSchemaDoc(it) },
                        // Requirements
                        doc.list("requirements") ap {
                            it.mapMut { VariableId.fromSchemaDoc(it) } },
                        // Variables
                        doc.list("variables") ap { docList ->
                            docList.mapSetMut { Variable.fromDocument(it) }
                        })
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Mechanic Id
 */
data class MechanicId(val value : String) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<MechanicId> = when (doc)
        {
            is DocText -> effValue(MechanicId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Mechanic Category
 */
data class MechanicCategory(val categoryId : MechanicCategoryId,
                            var label : String,
                            var description : String)
                             : Serializable
{


    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<MechanicCategory> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::MechanicCategory,
                        // Category Id
                        doc.at("id") ap { MechanicCategoryId.fromSchemaDoc(it) },
                        // Label
                        doc.text("label"),
                        // Description
                        doc.text("description")
                        )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}



/**
 * Mechanic Category Id
 */
data class MechanicCategoryId(val value : String) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<MechanicCategoryId> = when (doc)
        {
            is DocText -> effValue(MechanicCategoryId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


