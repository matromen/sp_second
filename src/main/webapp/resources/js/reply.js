/**
 * 
 */

console.log("reply js file.........");

var replyJsService = (function(){
	function replyList(pobj, callback, error){
		$.get("/reply/replyList/"+pobj.rseq+"/"+pobj.rpage, function(result){
			if(callback){
				callback(result);
			}
		}, "json").fail(function(xhr, status, err){
			if(error){
				error(status);
			}
		});
	}
	
	function replyWritePro(replyJson, callback, error){
		$.ajax({
			type:"post",
			url:"/reply/replyWritePro",
			
			data:JSON.stringify(replyJson),
			contentType:"application/json; charset=utf-8",
			beforeSend:function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			
			success: function(result, status, xhr){
				if(callback){
					callback(result);
				}
			},
			error: function(xhr, status, er){
				if(error){
					error(status);
				}
			}
		});
	}
	
	function replyUpdateForm(rseq, callback, error){
		$.get("/reply/"+rseq, function(result){
			if(callback){
				callback(result);
			}
		}, "json").fail(function(xhr, status, err){
			if(error){
				error(status);
			}
		});
	}
	
	function replyUpdatePro(replyJson, callback, error){
		$.ajax({
			type:"put",
			url:"/reply/"+$("#replyModal").data("rseq"),
			
			data:JSON.stringify(replyJson),
		    contentType:"application/json; charset=utf-8",
			beforeSend:function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			
			success:function(result, status, xhr){
				alert(result);
				
				if(callback){
					callback(result);
				}
			},
			error:function(xhr, status, err){
				if(error){
					error(status);
				}
			}
		});
	}
	
	function replyDeletePro(replyJson, callback, error){
		$.ajax({
			type:"delete",
			url:"/reply/"+$("#replyModal").data("rseq"),
			
			data:JSON.stringify(replyJson),
			contentType:"application/json; charset=utf-8",
			beforeSend:function(xhr){
				xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
			},
			
			success:function(result, status, xhr){
				alert(result);
				
				if(callback){
					callback(result);
				}
			},
			error : function(xhr, status, err){
				if(error){
					error(status);
				}
			}
		});
	}
	
	return {replyList:replyList, replyWritePro:replyWritePro, replyUpdateForm:replyUpdateForm, replyUpdatePro:replyUpdatePro, replyDeletePro,replyDeletePro};
})();