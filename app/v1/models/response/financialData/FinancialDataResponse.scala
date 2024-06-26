/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package v1.models.response.financialData

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class LineItemInterestDetails(currentInterestRate: Option[BigDecimal],
                                   interestPostedAmount: Option[BigDecimal],
                                   interestAccruingAmount: Option[BigDecimal],
                                   interestStartDate: Option[String])

object LineItemInterestDetails {
  implicit val format: OFormat[LineItemInterestDetails] = Json.format[LineItemInterestDetails]
}

case class LineItemDetail(
                          chargeDescription: Option[String],
                          periodFromDate: Option[String],
                          periodToDate: Option[String],
                          netDueDate: Option[String],
                          amount: Option[BigDecimal],
                          lineItemInterestDetails: Option[LineItemInterestDetails])

object LineItemDetail {
  implicit val format: OFormat[LineItemDetail] = Json.format[LineItemDetail]
}

case class DocumentInterestTotals(interestPostedAmount: Option[BigDecimal],
                                  interestAccruingAmount: Option[BigDecimal])

object DocumentInterestTotals {
  implicit val format: OFormat[DocumentInterestTotals] = Json.format[DocumentInterestTotals]
}

case class DocumentDetail(postingDate: Option[String],
                          issueDate: Option[String],
                          documentTotalAmount: Option[BigDecimal],
                          documentClearedAmount: Option[BigDecimal],
                          documentInterestTotals: Option[DocumentInterestTotals],
                          documentOutstandingAmount: Option[BigDecimal],
                          lineItemDetails: Option[Seq[LineItemDetail]])

object DocumentDetail {
  implicit val reads: Reads[DocumentDetail] = (
    (JsPath \ "postingDate").readNullable[String] and
      (JsPath \ "issueDate").readNullable[String] and
      (JsPath \ "documentTotalAmount").readNullable[BigDecimal] and
      (JsPath \ "documentClearedAmount").readNullable[BigDecimal] and
      (JsPath \ "documentInterestTotals").readNullable[DocumentInterestTotals] and
      (JsPath \ "documentOutstandingAmount").readNullable[BigDecimal] and
      (JsPath \ "lineItemDetails").readNullable[Seq[LineItemDetail]]
    )(DocumentDetail.apply _)

  implicit val writes: OWrites[DocumentDetail] = Json.writes[DocumentDetail]
}


case class AdditionalReceivableTotalisations(totalAccountPostedInterest: Option[BigDecimal],
                                             totalAccountAccruingInterest: Option[BigDecimal])

object AdditionalReceivableTotalisations {
  implicit val format: OFormat[AdditionalReceivableTotalisations] = Json.format[AdditionalReceivableTotalisations]

}

case class Totalisations(totalOverdue: Option[BigDecimal],
                        totalNotYetDue: Option[BigDecimal],
                        totalBalance: Option[BigDecimal],
                        totalCredit: Option[BigDecimal],
                        totalCleared: Option[BigDecimal],
                        additionalReceivableTotalisations: Option[AdditionalReceivableTotalisations])

object Totalisations {
  implicit val reads: Reads[Totalisations] = (
    (JsPath \ "targetedSearch_SelectionCriteriaTotalisation" \ "totalOverdue").readNullable[BigDecimal] and
      (JsPath \ "targetedSearch_SelectionCriteriaTotalisation" \ "totalNotYetDue").readNullable[BigDecimal] and
      (JsPath \ "targetedSearch_SelectionCriteriaTotalisation" \ "totalBalance").readNullable[BigDecimal] and
      (JsPath \ "targetedSearch_SelectionCriteriaTotalisation" \ "totalCredit").readNullable[BigDecimal] and
      (JsPath \ "targetedSearch_SelectionCriteriaTotalisation" \ "totalCleared").readNullable[BigDecimal] and
      (JsPath \ "additionalReceivableTotalisations").readNullable[AdditionalReceivableTotalisations]
    )(Totalisations.apply _)

  implicit val writes: OWrites[Totalisations] = Json.writes[Totalisations]
}


case class FinancialDataResponse(totalisations: Option[Totalisations],
                                 documentDetails: Option[Seq[DocumentDetail]])

object FinancialDataResponse {
  implicit val reads: Reads[FinancialDataResponse] = (
    (JsPath \ "getFinancialData" \ "financialDetails" \ "totalisation").readNullable[Totalisations] and
      (JsPath \ "getFinancialData" \ "financialDetails" \ "documentDetails").readNullable[Seq[DocumentDetail]]
    )(FinancialDataResponse.apply _)

  implicit val writes: OWrites[FinancialDataResponse] = Json.writes[FinancialDataResponse]}

case class FinancialDataErrors(
                            failures: List[FinancialDataError]
                          )

object FinancialDataErrors {
  implicit val format: OFormat[FinancialDataErrors] = Json.format[FinancialDataErrors]
}

case class FinancialDataError(
                         code: String,
                         reason: String
                       )

object FinancialDataError {
  implicit val format: OFormat[FinancialDataError] = Json.format[FinancialDataError]
}