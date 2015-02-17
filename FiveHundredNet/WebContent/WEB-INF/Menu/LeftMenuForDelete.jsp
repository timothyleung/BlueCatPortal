<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<table>
	<tr>
		<td align="left" valign="top">
			<table style="width: 170">
				<tr>
					<td>
						<ul>
							<li ${oneIP}><a href="?choose=OneIPAddress">Single IP Address</a></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td>
						<ul>
							<li ${oneMac}><a href="?choose=OneMACAddress">Single MAC Address</a></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td>
						<ul>
							<li ${multiAddress}><a href="?choose=MultiMACAddress">Bulk IP/MAC Address</a></li>
						</ul>
					</td>
				</tr>
			</table>
		</td>
		<td><jsp:include page="${ContentPage}"></jsp:include></td>
	</tr>
</table>