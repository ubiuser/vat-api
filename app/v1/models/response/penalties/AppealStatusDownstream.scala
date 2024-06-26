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

package v1.models.response.penalties

import play.api.libs.json
import utils.enums.Enums

sealed trait AppealStatusDownstream {
  def toUpstreamAppealStatus: AppealStatusUpstream
}

object AppealStatusDownstream {
  case object `A` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`under-appeal`
  }

  case object `B` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-upheld`
  }

  case object `C` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-rejected`
  }

  case object `03` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-rejected`
  }

  case object `91` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-rejected`
  }

  case object `92` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-upheld`
  }

  case object `93` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-upheld`
  }

  case object `94` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`appeal-rejected`
  }

  case object `99` extends AppealStatusDownstream {
    override def toUpstreamAppealStatus: AppealStatusUpstream = AppealStatusUpstream.`cannot-be-appealed`
  }

  implicit val format: json.Format[AppealStatusDownstream] = Enums.format[AppealStatusDownstream]
}