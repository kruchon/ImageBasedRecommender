MATCH
  (u:User{externalId:{userId}})-[i:INTERESTED_IN]->(t:Token)-[path:NEAREST_TO*0..{searchDepth}]-(last:Token)-[r:RECOGNIZED_IN]->(img:Image)
WITH u, img.externalId AS imgId, path, r.probability AS probability, i, t
MATCH (u)-[i_all:INTERESTED_IN]->(:Token)
WITH imgId, t.word + [p IN path | endNode(p).word] AS path, probability,
     (reduce(sum = 0, p IN path | sum - p.weight) + 100 )/ 100 AS pathWeight,
1 - i.interestDegree / sum(i_all.interestDegree)
AS interestWeight, t, u
WITH interestWeight + pathWeight + probability AS weight, imgId
WITH imgId, min(weight) AS weight
  ORDER BY weight
  WHERE weight is not null
RETURN imgId, weight