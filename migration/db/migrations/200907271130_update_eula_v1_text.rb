require "eula"
class UpdateEulaV1Text < ActiveRecord::Migration
  def self.up
    eula = Eula.find(:first)
    eula.legaltext = "N4 SYSTEMS PRODUCT
LICENSE AGREEMENT

BY CLICKING THE \"I ACCEPT\" BOX, OR DOWNLOADING OR INSTALLING OR USING THE FIELD ID SERVICE AND/OR THE SOFTWARE, AS APPLICABLE, THAT YOU HAVE SELECTED IN THE ORDERING PROCESS, AS DEFINED BELOW, YOU ARE AGREEING ON BEHALF OF THE ENTITY USING THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE (\"LICENSEE\") THAT COMPANY WILL BE BOUND BY AND IS BECOMING A PARTY TO THIS N4 SYSTEMS PRODUCT LICENSE AGREEMENT (\"AGREEMENT\") AND THAT YOU HAVE THE AUTHORITY TO BIND COMPANY. IF COMPANY DOES NOT AGREE TO ALL OF THE TERMS OF THIS AGREEMENT, DO NOT SELECT THE \"I ACCEPT\" BOX AND DO NOT USE THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE. COMPANY HAS NOT BECOME A LICENSEE OF, AND IS NOT AUTHORIZED TO USE THE SERVICE AND/OR THE SOFTWARE, AS APPLICABLE UNLESS AND UNTIL IT HAS AGREED TO BE BOUND BY THESE TERMS. THE \"EFFECTIVE DATE\" FOR THIS AGREEMENT SHALL BE THE DAY YOU CHECK THE \"I ACCEPT\" BOX.

WHEREAS N4 SYSTEMS INC. (“N4 Systems”) has developed the N4 Systems Product which is comprised of a unique software architecture together with additional optional software modules which may be licensed from N4 Systems from time to time;

AND WHEREAS N4 Systems has agreed to grant Company (the ”Licensee”) certain rights to use the N4 Systems Product on the terms and conditions herein set forth;

NOW THEREFORE the parties hereto agree as follows:


1. DEFINITIONS
Unless otherwise specified the following terms will have the meanings ascribed to them as follows:

1.1. “N4 Systems Product“ shall mean the computer software and software solutions, known commonly as “Field ID,” used in connection with the inspection, maintenance, service and repair of equipment (known commercially and referred to herein as “Field ID™”), in machine readable object code form, which may be combined or embodied in any medium whatsoever, consisting of a set of logical instructions and information which guide the functioning of a processor, and which shall include Updates and Upgrades provided from time to time by N4 Systems during the Term, and all information, ephemeral aspects, so-called “look & feel”, graphic design, user interface design, know how, systems and processes concerning such computer program, including without limitation such computer program's operational and functional specifications set out in documentation provided or made available by N4 Systems with the N4 Systems Product.

1.2. “Business Day“ shall mean Monday to Friday other than statutory holidays in the province of Ontario, Canada.

1.3. “Confidential Information” shall mean confidential and secret information including, but not limited to, trade secrets, processes, methods, ideas, algorithms plans, software source code, technical specifications, engineering data, computer software programs, manufacturing know-how, or other information relating to, incorporated in or forming part of  the N4 Systems Product, the product specifications and Documentation and any other information which would be reasonably considered to be confidential, including the terms of this Agreement.

1.4. “Documentation“ means the human-readable documentation that is delivered with the N4 Systems Product.

1.5. “Effective Date”  means the date referred to on the last page of this Agreement.

1.6. “License Fee” shall mean the amount payable to N4 Systems in consideration of the grant of any license for the N4 Systems Product as agreed upon between the parties.

1.7. “Maintenance and Support Services“  shall have the meaning set out in Section hereof.

1.8. “Maintenance Fee“ shall mean the fees payable by Licensee, as agreed upon from time to time, in connection with the provision by N4 Systems of the Maintenance and Support Services.

1.9. “Term“ shall have the meaning set out in Section  hereof.

1.10. “Update” shall mean a release of the N4 Systems Product which consists of minor corrections, bug fixes and minor enhancements to the previous version released by N4 Systems to Licensee.

1.11. “Upgrade” shall mean a release of the N4 Systems Product which consists of a new version with substantial enhancements, added functionality or new features from the previous version released by N4 Systems to Licensee.


2. GRANT AND RESTRICTIONS

2.1. Upon payment of the License Fee, N4 Systems grants Licensee a restricted, non-transferable and non‑exclusive license to use the N4 Systems Product which shall be hosted by N4 Systems and to use the Documentation and N4 Systems Product in object-code format solely for Licensee's professional use.  The N4 Systems Product shall not be used outside of Licensee's normal course of business.  Licensee may only permit its employees to use the  N4 Systems Product.  Except as expressly provided herein, the Licensee shall not permit third parties to have access to or use the N4 Systems Product.

2.2. Licensee may not download, copy or reproduce  the N4 Systems Product. 

2.3. Licensee shall not, and shall not permit any one else to reverse engineer, decompile, disassemble, or otherwise reduce the N4 Systems Product to any human‑readable form.  Licensee shall not modify, adapt, alter, edit, correct, translate, publish, sell, transfer, assign, convey, rent, lease, loan, pledge, sublicense, distribute, export, enhance, or create derivative works based upon the N4 Systems Product, in whole or part, or otherwise grant or transfer rights to the N4 Systems Product or the rights granted herein in any form or by any media (electronic, mechanical, photocopy, recording, or otherwise).  


3. LICENSE FEE

3.1. Licensee shall pay the License Fee, payable on the effective date of this Agreement. 

3.2. In addition to the License Fee, Licensee shall pay all taxes, however designated, including sales and use taxes, goods and services, duties and tariffs and other governmental charges payable in relation to its license and use of the N4 Systems Product.  

3.3. If the License Fee or any other amount payable to N4 Systems hereunder is not paid when due, interest shall accrue and be payable by the Licensee to N4 Systems at the rate of eighteen (18%) percent per annum, payable monthly.


4. TERM OF AGREEMENT
With respect to a license for which a License Fee has been paid, this Agreement shall become effective as of the date where Licensee has accepted the terms and conditions of this Agreement and shall remain in effect [in perpetuity] or [for the term mutually agreed upon in writing by the parties] unless earlier terminated in accordance with the terms of this Agreement.  


5. Limitation of Warranty

5.1. EXCEPT AS EXPRESSLY STATED IN THIS AGREEMENT, THE N4 SYSTEMS PRODUCT AND DOCUMENTATION ARE PROVIDED ON AN “AS IS“ BASIS AND THERE ARE NO WARRANTIES, REPRESENTATIONS OR CONDITIONS, EXPRESSED OR IMPLIED, WRITTEN OR ORAL, ARISING BY STATUTE, OPERATION OF LAW OR OTHERWISE, REGARDING THE N4 SYSTEMS PRODUCT OR ANY OTHER PRODUCT OR SERVICE PROVIDED HEREUNDER OR IN CONNECTION HEREWITH.  

5.2. N4 SYSTEMS DISCLAIMS ANY IMPLIED WARRANTY OR CONDITION OF MERCHANTABLE QUALITY, MERCHANTABILITY, FUNCTIONALITY, DURABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  

5.3. N4 SYSTEMS DOES NOT WARRANT THAT THE N4 SYSTEMS PRODUCT WILL MEET LICENSEE'S REQUIREMENTS OR THAT ITS OPERATION WILL BE UNINTERRUPTED,  ERROR FREE OR SECURE BEYOND WHAT IS OTHERWISE WARRANTED IN THE DOCUMENTATION OR THAT ALL CONTENT OR DATA DELIVERED OR PROVIDED UNDER THIS AGREEMENT OR THROUGH USE OF THE N4 SYSTEMS PRODUCT WILL BE APPROPRIATE OR APPLICABLE TO LICENSEE'S USE, N4 SYSTEMS DISCLAIMS ANY LIABILITY FOR ANY CONSEQUENCES DUE TO USE, MISUSE OR INTERPRETATION OF INFORMATION CONTAINED, OR NOT CONTAINED, IN THE N4 SYSTEMS PRODUCT OR THE DOCUMENTATION. 

5.4. N4 SYSTEMS DOES NOT WARRANT THAT THE N4 SYSTEMS PRODUCT WILL OPERATE WITH ANY OTHER COMPUTER PROGRAM NOT SPECIFIED IN THE DOCUMENTATION.

5.5. NO REPRESENTATION OR OTHER AFFIRMATION OF FACT, INCLUDING BUT NOT LIMITED TO STATEMENTS REGARDING PERFORMANCE OF THE N4 SYSTEMS PRODUCT, WHICH IS NOT CONTAINED IN THIS AGREEMENT, SHALL BE DEEMED TO BE A WARRANTY BY N4 SYSTEMS.  

5.5.1. In exercising its rights under this Agreement, Licensee will give and make no warranties or representations on behalf of N4 Systems as to quality, merchantable quality, fitness for a particular use or purpose or any other features of the N4 Systems Product, or other products or services provided by N4 Systems except as described in published documentation relating to the N4 Systems Product provided by N4 Systems, or other products or services provided by N4 Systems made generally commercially available by N4 Systems.


6. LICENSEE OBLIGATIONS AND ACKNOWLEDGEMENTS

6.1. Licensee acknowledges that it is responsible to create back-up records of all information entered by Licensee into the N4 Systems Product.

6.2. If the N4 Systems Product fails to perform in accordance with the Documentation, Licensee shall promptly advise N4 Systems of the defect and shall assist N4 Systems in identifying the defect.

6.3. Licensee acknowledges and agrees that all information and data provided, supplied or entered into the N4 Systems Product may be relied upon by third parties.  Licensee represents and warrants that all information and data provided, supplied or entered into by Licensee into the N4 Systems Product shall be true, correct and complete.


7. PRIVACY AND USE OF INFORMATION

7.1. Licensee shall at all times comply with all relevant privacy legislation, regulations and any privacy related professional obligations applicable in the use of the N4 Systems Product and, without limiting the generality of the foregoing, the collection, use and disclosure of data forming part of, incorporated or used in connection with the N4 Systems Product. 

7.2. Licensee acknowledges and agrees that N4 Systems may gather, collect, utilize, market and sell  information relating to its clients, usage patterns, usage data, utilized or contributed data provided, entered or supplied by the Licensee in connection with this Agreement (“Usage Data”) provided that any such use shall only be made for internal statistical purposes in an aggregate and anonymous format only.  On termination of this Agreement, N4 Systems shall be entitled to retain and use the Usage Data.


8. MAINTENANCE AND TECHNICAL SUPPORT
If requested, N4 Systems will provide technical support to Licensee (the “Maintenance and Support Services”) set forth in attached Schedule A, in consideration for the payment by Licensee of the Maintenance Fee.  The Maintenance Support Services shall be provided by N4 Systems for a period to be mutually agreed upon in writing from time to time.


9. OWNERSHIP AND CONFIDENTIALITY

9.1. Licensee hereby acknowledges and agrees that all right, title and interest in and to the N4 Systems Product, in whole or in part, and including, without limitation, all patent, copyright, trade-marks, trade secret and all other intellectual and industrial property rights in such N4 Systems Product and the structure, sequence and organization of same, and the media on which such material is contained shall belong to N4 Systems , and that Licensee’s sole rights thereto shall be only those rights granted by N4 Systems pursuant to this Agreement. Licensee further agrees and acknowledges that N4 Systems has and reserves the exclusive, world-wide right in perpetuity to protect the N4 Systems Product and all product specifications including its structure, sequence and organization, screens and any part thereof, under any laws for the protection of intellectual and industrial property, including without limitation, trade secrets, trademarks, copyrights, industrial designs and patents.

9.2. The N4 Systems Product, Documentation, product specifications and all documentation and information, including without limitation, so-called “look and feel” aspects, design and presentation, trade secrets, drawings and technical and marketing information which is or has been supplied by N4 Systems to Licensee, acquired or developed by Licensee is hereby deemed to be Confidential Information and shall be held in trust and confidence for, and on behalf of, N4 Systems, by Licensee and its employees, and shall not be disclosed by Licensee or used by Licensee for any purpose other than as strictly permitted under this Agreement without N4 Systems' prior written consent.

9.3. Licensee shall treat the Confidential Information in strict confidence and shall not disclose, transfer, copy, reproduce, electronically transmit, store or maintain, remanufacture or in any way duplicate all, or any part of, the Confidential Information except in accordance with the terms and conditions of this Agreement.  Licensee shall be directly liable for the acts or omissions of its employees, agents and contractors with respect to such confidentiality obligations.  Licensee agrees to protect the Confidential Information with the same standard of care and procedures which it uses to protect its own trade secrets, proprietary information and other confidential information and, in any case, not less than a reasonable standard of care.

9.4. The obligations of confidentiality imposed by Section  shall not apply, or shall cease to apply, to any Confidential Information: which at the time of disclosure is within the public domain, other than through a breach of this Agreement; which after disclosure becomes readily and lawfully available to the industry or the public, other than through a breach of this Agreement; which Licensee can establish, by documented and competent evidence, was in its possession prior to the date of disclosure of such Confidential Information by N4 Systems;

9.5. In the event that the Licensee is required by law or direction of a regulatory authority to disclose Confidential Information, the Licensee shall provide N4 Systems with prompt written notice of the requirement to permit N4 Systems to seek a protective order or other appropriate remedy to prevent such disclosure.  In the event that such protective order or other remedy is not obtained, the Licensee shall only disclose such portion of the Confidential Information as may be required to be disclosed.

9.6. Licensee undertakes to comply with its obligations according to the legislation in the jurisdictions in which it carries on business.


10. INTELLECTUAL  PROPERTY  RIGHTS  AND INDEMNIFICATION

10.1. Licensee agrees to take adequate steps to protect the N4 Systems Product from unauthorized disclosure or use.  Licensee shall continually use its best efforts to protect N4 Systems'  trade-marks, trade names, patents, copyrights, and other proprietary rights, but shall not initiate legal action against third parties for infringement thereof. Licensee shall promptly notify N4 Systems of any infringement or improper or unauthorized use of which it has actual knowledge.

10.2. The N4 Systems Product, Documentation and the product specifications are copyrighted and title to all copies is retained by N4 Systems.   Licensee will not alter, remove, cover or otherwise obscure any copyright notices, trade‑mark notices and any other intellectual property rights attaching to or displayed on the N4 Systems Product,  Documentation and any other material and documentation made available under this Agreement.  The Licensee will comply with all reasonable directions issued by N4 Systems from time to time regarding the form and placement of any and all relevant proprietary rights notices in or on the N4 Systems Product, or Documentation or any other related media, packaging or material.

10.3. N4 Systems shall indemnify Licensee against all claims made against Licensee alleging that any use of the N4 Systems Product or Documentation constitutes an infringement of copyright, patent, trade-mark or trade secret rights.  N4 Systems shall have exclusive carriage of the defence of any such claim made against Licensee and has the exclusive right to settle the claim.  Licensee shall cooperate fully in the conduct of the defence.  Licensee shall notify N4 Systems forthwith upon any claim being made against Licensee that its use of the N4 Systems Product is alleged to be an infringement of the intellectual property rights of another.

10.4. Licensee shall, at its expense, defend and indemnify N4 Systems in any suit, claim or proceeding brought against N4 Systems arising out of or resulting from the use of the N4 Systems Product by Licensee for purposes for which it was not authorized hereunder and the installation and integration and the use of the N4 Systems Product in combination with software or other equipment or products not supplied by N4 Systems PROVIDED:

10.4.1. Licensee is notified promptly in writing of the nature of such action, threat of action or claim which comes to N4 Systems' knowledge; and

10.4.2. N4 Systems renders reasonable assistance as required by Licensee.

10.5. Licensee shall consult with N4 Systems with respect to the defense and settlement of any such claim.


11. LIABILITY
EXCEPT AS OTHERWISE EXPRESSLY STATED IN THIS AGREEMENT, N4 Systems SHALL NOT BE LIABLE TO THE LICENSEE OR ANY PARTY MAKING A CLAIM AGAINST N4 Systems THROUGH THE LICENSEE FOR SPECIAL, INCIDENTAL, PUNITIVE, CONSEQUENTIAL OR INDIRECT DAMAGES OR LOSS (INCLUDING DEATH AND PERSONAL INJURY), IRRESPECTIVE OF THEIR CAUSE, NOTWITHSTANDING THAT A PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH LOSS OR DAMAGE, NOR FOR ANY CLAIMS FOR SUCH LOSS OR DAMAGE INSTITUTED AGAINST A PARTY OR ITS CUSTOMERS BY ANY THIRD PARTY.  WITHOUT LIMITING THE GENERALITY OF THE FOREGOING, N4 Systems ASSUMES NO LIABILITY FOR ANY LOSS OF: USE, DATA, OR THE PROVISION OF INCORRECT DATA, INCOME, BUSINESS, PROFIT, ANTICIPATED REVENUE OR ANY OTHER COMMERCIAL OR ECONOMIC LOSS (EVEN IF N4 SYSTEMS HAD BEEN ADVISED OF THE POSSIBILITY OF SUCH LOSS). IN NO EVENT SHALL N4 Systems'  LIABILITY TO ANY PERSON OR ENTITY, WHETHER IN CONTRACT, TORT (INCLUDING NEGLIGENCE), OR OTHERWISE, EXCEED THE LICENSE FEE PAID BY LICENSEE UNDER AND DURING THE TERM OF THIS AGREEMENT.


12. TERMINATION 

12.1. If any of the following conditions arise during the Term, N4 Systems shall have the right, at its option, to terminate this Agreement by giving a written notice of such termination to Licensee, whereupon this Agreement shall immediately cease and terminate:

12.1.1. if any voluntary petition in bankruptcy or any petition for similar relief shall be filed by Licensee;

12.1.2. if any involuntary petition in bankruptcy shall be filed against Licensee and shall not have been dismissed or remains uncontested for a period of thirty (30) days from the filing thereof under any federal, provincial or state bankruptcy, insolvency or similar legislation;

12.1.3. if a receiver shall be appointed for Licensee of its property and such receiver shall not have been dismissed or remains uncontested for a period of thirty (30) days from the date of such appointment; 

12.1.4. if Licensee shall make an assignment for the benefit of creditors or admit in writing its inability to meet its debts as they mature; or

12.1.5. if Licensee breaches its confidentiality obligations pursuant to Section hereof.

12.2. Subject to the N4 Systems' right to immediately terminate the Agreement pursuant to Section  hereof, N4 Systems shall have the right to terminate this Agreement if Licensee fails to perform any other substantial obligation herein on its part to be performed, including but not limited to payment, and the failure continues for at least thirty (30) days after the giving of written notice of such failure to Licensee by N4 Systems .

12.3. Effective upon termination hereof, Licensee shall:

12.3.1. immediately make all payments and perform any other obligations of Licensee arising hereunder within thirty (30) days of termination;

12.3.2. immediately cease to access, use and/or permit to use the N4 Systems Product and Documentation in any manner whatsoever;

12.3.3. as directed at the sole option of N4 Systems, immediately destroy or return all copies of the N4 Systems Product and Documentation including all electronically stored copies thereof and back up copies and related materials and any N4 Systems Confidential Information in Licensee's possession, with an affidavit of the Licensee or a senior officer of the Licensee attesting to completion of this task.

12.4. Termination hereunder shall be without prejudice to any other right or remedy to which either party may be entitled hereunder in law.


13. ASSIGNMENT
Licensee shall not assign this Agreement or any right hereunder or assign or delegate any obligation hereunder without the express written consent of N4 Systems.


14. RELATIONSHIP
This Agreement is entered between separate entities and neither is the agent of the other for any purpose whatsoever. Licensee and N4 Systems are independent contractors and neither has any power nor will it represent itself as having any power to in any way bind or obligate the other or to assume or create any expressed or implied obligation or responsibility on behalf of the other or in other's name, and neither party shall have authority to represent itself as agent of the other.  This Agreement shall not be construed as constituting Licensee and N4 Systems as partners or to create any other form of legal association which would impose liability upon one party for the act or failure to act of the other.


15. ENTIRE AGREEMENT

15.1. This Agreement and its Schedules constitute the entire agreement between the parties with respect to the subject matter set forth and supersedes any and all prior agreements between the parties either oral or written.

15.2. Unless specifically otherwise provided for herein, this Agreement may not be amended, waived or extended, in whole or in part, except by a writing signed by both parties hereto.


16. NOTICES

16.1. Any notice regarding terms and conditions of this Agreement, to be given to any of all of the parties hereunder shall be in writing and may be given by delivering the same to the parties to the addresses, e‑mail addresses and facsimile numbers set out at the beginning of this Agreement.

16.2. Any such notice shall be delivered by registered mail (return receipt requested), by messenger or by hand or by facsimile or e-mail provided evidence of transmission if produced at point of origination.  Any such notice shall be deemed to have been given on the date on which it was delivered if delivered by hand, messenger facsimile or e-mail and on the fifth (5th) business day following posting if given by registered mail and on the next business day after facsimile transmission.  In the case of postal disruption, all notices pursuant to this section  are to be delivered by messenger, hand, facsimile or e-mail but not registered mail.

16.3. Any party hereto may change its address for service from time to time by notice given to the other parties hereto in accordance with the foregoing.


17. SEVERABILITY
Should any part of this Agreement be found to be invalid by a court of competent jurisdiction, the remainder of this Agreement shall continue in full force and effect.


18. SURVIVAL
The obligations set out in Sections 3, 6, 7, 9, 10, 11, 12, 14, 15, 17, 19, 20 and 22 hereof shall survive the termination or expiration of this Agreement.


19. WAIVER
No waiver by either party of any breach by the other party of any provision of this Agreement shall, unless in writing, be deemed or construed to be a waiver of any succeeding or other breach of such provision or as a waiver of the provision itself.


20. CURRENCY
All references to currency in this Agreement shall be references to the lawful currency of United States of America unless otherwise agreed upon by the parties. 


21. APPLICABLE LAW
This Agreement shall be governed by and construed in accordance to the laws applicable in the Province of Ontario, Canada, without giving effect to the principles of conflicts of law and excluding that body of law applicable to choice of law and excluding the United Nations Convention for the International Sale of Goods, if applicable.  Any claim or court proceeding brought by N4 Systems in relation with this Agreement may be presented in the Province of Ontario, Canada.  Licensee agrees that the courts of the Province of Ontario, Canada constitute the appropriate forum for any claim or court proceeding in relation with this Agreement and submits to the exclusive jurisdiction of such courts.


22. DISPUTE RESOLUTION
Any dispute, controversy or claim arising out of or in connection with this Agreement, including any question regarding its negotiation, existence, validity, breach or termination, shall be referred to and finally resolved by arbitration in accordance with the provisions of The Arbitration Act, 1991 (Ontario).  The arbitral tribunal shall be composed of one arbitrator.  The place of arbitration shall be the City of Toronto, Province of Ontario, Canada.  The language of the arbitration shall be English.  Each of the parties hereto hereby irrevocably attorns to the exclusive jurisdiction of any such arbitration. Arbitration shall be a condition precedent to any action taken in a court.  The parties attorn to the jurisdiction of the Courts of the Province of Ontario, sitting at Toronto, Ontario.  The parties waive any right they might have to a trial by jury.


SCHEDULE  A
TO LICENSE AGREEMENT


1. Maintenance and technical support to be provided by N4 Systems on the following terms:

1.1. N4 Systems agrees to provide technical support to Licensee for the N4 Systems Product in accordance with this Schedule for the Maintenance Fee payable in advance.

1.2. N4 Systems will provide technical support for the N4 Systems Product and any Updates and Upgrades, such that the N4 Systems Product will operate in conformity with the Documentation, in all material respects.

1.3. N4 Systems will provide telephone, fax, web-based, and electronic mail support on technical issues between the hours of 8:00 and 16:00 (Eastern Time) on business days. 

1.4. N4 Systems will provide work arounds, error corrections and software fixes for reported problems within a commercially reasonable period of time taking into account the priority level of the reported problem. 

1.5. The Licensee will report incidents describe the nature of the incident and provide details of the circumstances of its occurrence.

1.6. From time to time, N4 Systems will make available to the client the following:

1.6.1. fixes for known bugs or errors in the N4 Systems Product;

1.6.2. available work arounds; and/or

1.6.3. resolutions, error corrections or bug fixes.

1.7. Maintenance and technical support does not include:

1.7.1. custom programming services;

1.7.2. training;

1.7.3. hardware and related supplies; 

1.7.4. any support services provided at the client site.

1.8. N4 Systems shall not be liable for delay or failure in performance resulting from acts beyond its control, including, but not limited to acts of God, acts of war, riot, fire, flood, or other disaster, acts of government, strike lockout or communication line or power failures.  Performance times shall be extended for a period of time equivalent to the period of delay."
    
    eula.save
  end
  
  def self.down
  end
end