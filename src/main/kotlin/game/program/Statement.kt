
package game.program


import effect.*
import game.engine.function.FunctionId
import game.engine.reference.DataReference
import lulo.document.*
import lulo.schema.Prim
import lulo.schema.Sum
import lulo.value.UnexpectedType
import lulo.value.UnknownCase
import lulo.value.ValueError
import lulo.value.ValueParser
import sun.rmi.runtime.Log
import java.io.Serializable


/**
 * Statement
 */
data class Statement(val bindingName : StatementBindingName,
                     val functionId : FunctionId,
                     val parameter1 : StatementParameter,
                     val parameter2 : Maybe<StatementParameter>,
                     val parameter3 : Maybe<StatementParameter>,
                     val parameter4 : Maybe<StatementParameter>,
                     val parameter5 : Maybe<StatementParameter>)
                     : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Statement> = when (doc)
        {
            is DocDict ->
            {
                apply(::Statement,
                        // Binding
                        doc.at("binding_name") ap { StatementBindingName.fromSchemaDoc(it) },
                        // Function Id
                        doc.at("function_id") ap { FunctionId.fromSchemaDoc(it) },
                        // Parameter 1
                        doc.at("parameter1") ap { StatementParameter.fromSchemaDoc(it) },
                        // Parameter 2
                        split(doc.maybeAt("parameter2"),
                                effValue<ValueError,Maybe<StatementParameter>>(Nothing()),
                                { effApply(::Just, StatementParameter.fromSchemaDoc(it)) }),
                        // Parameter 3
                        split(doc.maybeAt("parameter3"),
                                effValue<ValueError,Maybe<StatementParameter>>(Nothing()),
                                { effApply(::Just, StatementParameter.fromSchemaDoc(it)) }),
                        // Parameter 4
                        split(doc.maybeAt("parameter4"),
                                effValue<ValueError,Maybe<StatementParameter>>(Nothing()),
                                { effApply(::Just, StatementParameter.fromSchemaDoc(it)) }),
                        // Parameter 5
                        split(doc.maybeAt("parameter5"),
                                effValue<ValueError,Maybe<StatementParameter>>(Nothing()),
                                { effApply(::Just, StatementParameter.fromSchemaDoc(it)) })
                )
            }
            else       -> effError(lulo.value.UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }

}


/**
 * Statement Binding
 */
data class StatementBindingName(val value : String) : java.io.Serializable
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<StatementBindingName> = when (doc)
        {
            is DocText -> effValue(StatementBindingName(doc.text))
            else       -> effError(UnexpectedType(DocType.TEXT, docType(doc), doc.path))
        }
    }

}


/**
 * Statement Parameter
 */
@Suppress("UNCHECKED_CAST")
sealed class StatementParameter : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<StatementParameter> = when (doc.case())
        {
            "statement_binding"       -> StatementParameterBindingName.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<StatementParameter>
            "program_parameter_index" -> StatementParameterProgramParameter.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<StatementParameter>
            "data_reference"          -> StatementParameterReference.fromSchemaDoc(doc.nextCase())
                                            as ValueParser<StatementParameter>
            else                      -> effError(UnknownCase(doc.case(), doc.path))
        }
    }

}


/**
 * Binding Parameter
 */
data class StatementParameterBindingName(val bindingName : StatementBindingName)
                : StatementParameter()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<StatementParameterBindingName> =
                effApply(::StatementParameterBindingName, StatementBindingName.fromSchemaDoc(doc))
    }

}


/**
 * Program Parameter Reference
 */
data class StatementParameterProgramParameter(val index : ProgramParameterIndex)
                : StatementParameter()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<StatementParameterProgramParameter> =
            apply(::StatementParameterProgramParameter, ProgramParameterIndex.fromDocument(doc))
    }

}


/**
 * Reference Parameter
 */
data class StatementParameterReference(val reference : DataReference) : StatementParameter()
{

    // -----------------------------------------------------------------------------------------
    // CONSTRUCTORS
    // -----------------------------------------------------------------------------------------

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<StatementParameterReference> =
            apply(::StatementParameterReference, DataReference.fromSchemaDoc(doc))
    }

}
