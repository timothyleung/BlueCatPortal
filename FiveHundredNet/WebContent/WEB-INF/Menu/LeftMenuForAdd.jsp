<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<table >
	<tr>
		<td align="left" valign="top">
			<table style="width: 170" >
				<tr>
					<td>
						<ul>
							<li ${oneMac}><a href="?choose=OneMACAddressShow">Single MAC Address</a></li>
						</ul>
					</td>
				</tr>
				<tr>
					<td>
						<ul>
							<li ${multiMac}><a href="?choose=MultiMACAddress">Bulk MAC Address</a></li>
						</ul>
					</td>
				</tr>
			</table>
		</td>
		<td><jsp:include page="${ContentPage}"></jsp:include></td>
	</tr>
</table>