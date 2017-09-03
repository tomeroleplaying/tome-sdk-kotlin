
package game.program


import effect.*
import game.engine.reference.DataReference
import lulo.document.DocDict
import lulo.document.DocType
import lulo.document.SchemaDoc
import lulo.document.docType
import lulo.value.UnexpectedType
import lulo.value.ValueError
import lulo.value.ValueParser
import java.io.Serializable



/**
 * Program Invocation
 */
data class Invocation(val programId : ProgramId,
                      val parameter1 : DataReference,
                      val parameter2 : Maybe<DataReference>,
                      val parameter3 : Maybe<DataReference>,
                      val parameter4 : Maybe<DataReference>,
                      val parameter5 : Maybe<DataReference>) : Serializable
{

    companion object
    {
        fun fromSchemaDoc(doc : SchemaDoc) : ValueParser<Invocation> = when (doc)
        {
            is DocDict ->
            {
                apply(::Invocation,
                      // Program Name
                      doc.at("program_id") ap { ProgramId.fromSchemaDoc(it) },
                      // Parameter 1
                      doc.at("parameter1") ap { DataReference.fromSchemaDoc(it) },
                      // Parameter 2
                      split(doc.maybeAt("parameter2"),
                            effValue<ValueError,Maybe<DataReference>>(Nothing()),
                            { effApply(::Just, DataReference.fromSchemaDoc(it)) }),
                      // Parameter 3
                      split(doc.maybeAt("parameter3"),
                            effValue<ValueError,Maybe<DataReference>>(Nothing()),
                            { effApply(::Just, DataReference.fromSchemaDoc(it)) }),
                      // Parameter 4
                      split(doc.maybeAt("parameter4"),
                            effValue<ValueError,Maybe<DataReference>>(Nothing()),
                            { effApply(::Just, DataReference.fromSchemaDoc(it)) }),
                      // Parameter 5
                      split(doc.maybeAt("parameter5"),
                            effValue<ValueError,Maybe<DataReference>>(Nothing()),
                            { effApply(::Just, DataReference.fromSchemaDoc(it)) })
                )
            }
            else -> effError(UnexpectedType(DocType.DICT, docType(doc), doc.path))
        }
    }


}
