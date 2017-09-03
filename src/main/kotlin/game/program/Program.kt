
package game.program


import effect.*
import game.engine.EngineValueType
import lulo.document.*
import lulo.schema.Prim
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable
import java.util.*



/**
 * Program
 */
data class Program(val programId : ProgramId,
                   val label : String,
                   val description : String,
                   val typeSignature : ProgramTypeSignature,
                   val statements : List<Statement>,
                   val resultBindingName: StatementBindingName)
                    : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Program> = when (doc)
        {
            is DocDict ->
            {
                effect.apply(::Program,
                        // Program Id
                        doc.at("program_id") ap { ProgramId.fromSchemaDoc(it) },
                        // Label
                        doc.text("label"),
                        // Description
                        doc.text("description"),
                        // Type Signature
                        doc.at("type_signature") ap { ProgramTypeSignature.fromSchemaDoc(it) },
                        // Statements
                        split(doc.maybeList("statements"),
                                effValue<ValueError,MutableList<Statement>>(mutableListOf()),
                                { it.mapMut { Statement.fromSchemaDoc(it) } }),
                        doc.at("result_binding_name") ap { StatementBindingName.fromSchemaDoc(it) })
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Program Type Signature
 */
data class ProgramTypeSignature(val parameter1Type : EngineValueType,
                                val parameter2Type : Maybe<EngineValueType>,
                                val parameter3Type : Maybe<EngineValueType>,
                                val parameter4Type : Maybe<EngineValueType>,
                                val parameter5Type : Maybe<EngineValueType>,
                                val resultType : EngineValueType)
                                 : java.io.Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ProgramTypeSignature> = when (doc)
        {
            is DocDict ->
            {
                apply(::ProgramTypeSignature,
                        // Parameter 1 Type
                        doc.at("parameter1_type") ap { EngineValueType.fromSchemaDoc(it) },
                        // Parameter 2 Type
                        split(doc.maybeAt("parameter2_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 3 Type
                        split(doc.maybeAt("parameter3_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 4 Type
                        split(doc.maybeAt("parameter4_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Parameter 5 Type
                        split(doc.maybeAt("parameter5_type"),
                                effValue<ValueError,Maybe<EngineValueType>>(Nothing()),
                                { effApply(::Just, EngineValueType.fromSchemaDoc(it)) }),
                        // Result Type
                        doc.at("result_type") ap { EngineValueType.fromSchemaDoc(it) }
                )
            }
            else       -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }

    }

}


/**
 * Program Id
 */
data class ProgramId(val value : String) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<ProgramId> = when (doc)

        {
            is DocText -> effValue(ProgramId(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Program Parameter
 */
data class ProgramParameterIndex(val value : Int) : Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromDocument(doc : SchemaDoc) : ValueParser<ProgramParameterIndex> = when (doc)
        {
            is DocNumber -> effValue(ProgramParameterIndex(doc.number.toInt()))
            else         -> effError(UnexpectedType(DocType.NUMBER, docType(doc), doc.path))
        }
    }

}

