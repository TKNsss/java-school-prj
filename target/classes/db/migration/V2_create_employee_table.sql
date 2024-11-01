CREATE TABLE [dbo].[Employees](
	[ID] [char](5) NOT NULL,
	[first_name] [nvarchar](30) NOT NULL,
	[last_name] [nvarchar](30) NOT NULL,
	[gender] [varchar](10) NOT NULL,
	[phone] [char](10) NOT NULL,
	[date] [date] NOT NULL,
	[address] [nvarchar](20) NOT NULL,
	[position] [varchar](20) NOT NULL,
 CONSTRAINT [PK_Employees] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]