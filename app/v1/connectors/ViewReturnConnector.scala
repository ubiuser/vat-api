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

package v1.connectors

import config.AppConfig
import play.api.http.Status

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}
import utils.pagerDutyLogging.{Endpoint, PagerDutyLogging, PagerDutyLoggingEndpointName}
import v1.controllers.UserRequest
import v1.models.errors.{ConnectorError, DesErrorCode, DesErrors}
import v1.models.outcomes.ResponseWrapper
import v1.models.request.viewReturn.ViewRequest
import v1.models.response.viewReturn.ViewReturnResponse

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ViewReturnConnector @Inject()(val http: HttpClient,
                                    val appConfig: AppConfig) extends BaseDownstreamConnector {

  def viewReturn(request: ViewRequest)(
    implicit hc: HeaderCarrier,
    ec: ExecutionContext,
    userRequest: UserRequest[_],
    correlationId: String): Future[DesOutcome[ViewReturnResponse]] = {

    import v1.connectors.httpparsers.StandardDesHttpParser._

    val vrn = request.vrn.vrn
    implicit val connectorError: ConnectorError =
      ConnectorError(vrn, hc.requestId.fold(""){ requestId => requestId.value})
    implicit val pagerDutyLoggingEndpointName: PagerDutyLoggingEndpointName.Value = Endpoint.RetrieveReturns.toLoggerMessage

    val queryParams: Seq[(String, String)] =
      Seq(
        "period-key" -> request.periodKey
      )

    get(
      uri = DesUri[ViewReturnResponse](s"vat/returns/vrn/$vrn"),
      queryParams = queryParams
    ).recover {
      case e =>
        val logDetails = s"request failed. ${e.getMessage}"

        errorLog(ConnectorError.log(
          logContext = "[ViewReturnConnector][viewReturn]",
          vrn = vrn,
          details = logDetails,
        ))

        PagerDutyLogging.log(
          pagerDutyLoggingEndpointName = Endpoint.RetrieveReturns.requestFailedMessage,
          status = Status.INTERNAL_SERVER_ERROR,
          body = logDetails,
          f = errorLog(_),
          affinityGroup = userRequest.userDetails.userType
        )

        Left(ResponseWrapper(correlationId, DesErrors(List(DesErrorCode("DOWNSTREAM_ERROR"))))) }
  }
}
